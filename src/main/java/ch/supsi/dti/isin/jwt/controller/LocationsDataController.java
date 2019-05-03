package ch.supsi.dti.isin.jwt.controller;

import ch.supsi.dti.isin.jwt.dao.UserRepository;
import ch.supsi.dti.isin.jwt.model.User;
import ch.supsi.dti.isin.jwt.service.JwtAddLocationResponse;
import ch.supsi.dti.isin.jwt.service.JwtGetLocationsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LocationsDataController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value ={"protected/addLocation/{location}"}, method = {RequestMethod.POST})
    public ResponseEntity<?> addNewLocation(@PathVariable String location, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        user.addLocation(location);
        userRepository.save(user);

        return ResponseEntity.ok(new JwtAddLocationResponse(user.getUsername(), location));
    }

    @RequestMapping(value ={"protected/getLocations"}, method = {RequestMethod.GET})
    public ResponseEntity<?> addNewLocation(HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails =
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(new JwtGetLocationsResponse(user.getUsername(), user.getLocations()));
    }

}
