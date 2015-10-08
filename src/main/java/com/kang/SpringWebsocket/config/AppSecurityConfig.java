package com.kang.SpringWebsocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	  auth.inMemoryAuthentication().withUser("tom").password("123456").roles("USER");
	  auth.inMemoryAuthentication().withUser("bill").password("123456").roles("ADMIN");
	  auth.inMemoryAuthentication().withUser("ken").password("123456").roles("ADMIN");
	  auth.inMemoryAuthentication().withUser("james").password("123456").roles("SUPERADMIN");
	}
 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.headers()
			.frameOptions().sameOrigin()
			.and()
		//關閉要連接endpoint的csrf，似乎是因為client連接endpoint是使用http post(不確定)。
		.csrf()
			.ignoringAntMatchers("/App/**")
			.and()
		.authorizeRequests()
			.antMatchers("/resources/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
			.and()
		.logout()
			.permitAll();

	  
	  
 
	}
	

}
