package com.ibm.cai.employee.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Security Configuration - HTTP Basic Authorizations.
 * 
 * @auhor rajasood@in.ibm.com
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	final Logger logger = LoggerFactory.getLogger(getClass());

	public final static HashMap<String, String> hash_map = new HashMap<String, String>();

	@Value("${nbc.db.threadcount}")
	private String Threadcount;

	@Value("${nbc.db.recordperCSV}")
	private String PageSize;

	@Value("${nbc.db.totalRecords}")
	private String totalRecords;

	public SecurityConfig() {
		super();
	}

	/**
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 * @Input Param HttpSecurity httpSecurit
	 * @return void
	 */
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("pass")).roles("ADMIN");
		System.out.println("***********" + passwordEncoder().encode("pass") + Threadcount + ":" + PageSize);
		hash_map.put("Threadcount", Threadcount);
		hash_map.put("PageSize", PageSize);
		hash_map.put("TotalRecords", totalRecords);

		System.out.println("****map" + SecurityConfig.hash_map.get("Threadcount"));
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				// .antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/anonymous*").anonymous().antMatchers("/*" /*,"/h2-console/"*/).permitAll().anyRequest()
				.authenticated().and().formLogin();
 http.headers().frameOptions().sameOrigin();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() throws Exception {
		return new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {

				logger.info("Invalid  Username/password Credentials");
				exception.addSuppressed(exception);

			}
		};
	}

}