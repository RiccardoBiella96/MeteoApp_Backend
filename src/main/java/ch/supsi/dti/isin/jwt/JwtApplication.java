package ch.supsi.dti.isin.jwt;

import ch.supsi.dti.isin.jwt.dao.UserRepository;
import ch.supsi.dti.isin.jwt.model.Authority;
import ch.supsi.dti.isin.jwt.model.AuthorityName;
import ch.supsi.dti.isin.jwt.model.User;
import ch.supsi.dti.isin.jwt.dao.AuthorityRepository;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@EnableJpaRepositories
@SpringBootApplication
public class JwtApplication {

	@Autowired
	private DataSource datasource;
	@Autowired
	private ApplicationContext webApplicationContext;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner loadData(UserRepository userRepository, AuthorityRepository authorityRepository) {
		return (args) -> {

			User user=userRepository.findByUsername("admin");

			if(user == null){

				Authority authorityAdmin=new Authority();
				authorityAdmin.setName(AuthorityName.ROLE_ADMIN);
				authorityAdmin=authorityRepository.save(authorityAdmin);

				Authority authorityUser=new Authority();
				authorityUser.setName(AuthorityName.ROLE_USER);
				authorityUser=authorityRepository.save(authorityUser);


				List<Authority> authorities = Arrays.asList(new Authority[] {authorityAdmin,authorityUser});


				user = new User();
				user.setAuthorities(authorities);
				user.setEnabled(true);
				user.setUsername("admin");
				user.setPassword(passwordEncoder.encode("admin"));

				user = userRepository.save(user);

			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}
}
