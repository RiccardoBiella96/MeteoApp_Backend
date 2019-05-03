package ch.supsi.dti.isin.jwt.controller.security;

import ch.supsi.dti.isin.jwt.security.JwtRegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import ch.supsi.dti.isin.jwt.dao.AuthorityRepository;
import ch.supsi.dti.isin.jwt.dao.UserRepository;
import ch.supsi.dti.isin.jwt.model.Authority;
import ch.supsi.dti.isin.jwt.model.AuthorityName;
import ch.supsi.dti.isin.jwt.model.User;
import ch.supsi.dti.isin.jwt.security.JwtAuthenticationRequest;
import ch.supsi.dti.isin.jwt.security.JwtTokenUtil;
import ch.supsi.dti.isin.jwt.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "public/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device, HttpServletResponse response) throws AuthenticationException, JsonProcessingException {

        // Effettuo l autenticazione
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Genero Token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        response.setHeader(tokenHeader,token);
        // Ritorno il token
        return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getUsername(),userDetails.getAuthorities()));
    }

    @RequestMapping(value={"public/register"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResponseEntity<?> registerUser(@RequestBody JwtRegisterRequest jwtRegisterRequest, Device device, HttpServletResponse response) throws JsonProcessingException
    {
        User user = new User();
        user.setAuthorities(Arrays.asList(new Authority[] { authorityRepository.findByName(AuthorityName.ROLE_USER) }));
        user.setEnabled(Boolean.valueOf(true));
        user.setUsername(jwtRegisterRequest.getUsername());
        user.setPassword(passwordEncoder.encode(jwtRegisterRequest.getPassword()));
        user = (User)userRepository.save(user);

        return ResponseEntity.ok(new JwtRegisterRequest(user.getUsername(), user.getAuthorities().toString()));
    }

    @RequestMapping(value = "protected/refresh-token", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(tokenHeader);
        UserDetails userDetails =
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            response.setHeader(tokenHeader,refreshedToken);

            return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getUsername(),userDetails.getAuthorities()));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}