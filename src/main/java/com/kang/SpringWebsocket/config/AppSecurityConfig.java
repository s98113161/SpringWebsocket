package com.kang.SpringWebsocket.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	LogoutHandler CustomLogoutHandler;
	@Autowired
	 DataSource dataSource;
	@Bean
	public CustomLogoutHandler customLogoutHandler() {
	    return new CustomLogoutHandler();
	}
	 @Bean
	    public SessionRegistry sessionRegistry() {
	        return new SessionRegistryImpl();
	    }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//	  auth.inMemoryAuthentication().withUser("tom").password("123456").roles("USER");
//	  auth.inMemoryAuthentication().withUser("bill").password("123456").roles("ADMIN");
//	  auth.inMemoryAuthentication().withUser("ken").password("123456").roles("ADMIN");
//	  auth.inMemoryAuthentication().withUser("james").password("123456").roles("SUPERADMIN");
		auth.jdbcAuthentication().dataSource(dataSource)
		  .usersByUsernameQuery(
		   "select username,password, enabled from users where username=?")
		  .authoritiesByUsernameQuery(
		   "select username, role from user_roles where username=?");
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
		//設定自己LogoutHandler，也許往後會要知道誰登出要做功能
	    .addLogoutHandler(CustomLogoutHandler)
	    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.permitAll();
		//這裡可以抓一個帳號可以連幾個人，這裡是2個
		http.sessionManagement().maximumSessions(2).sessionRegistry(sessionRegistry());
		
	}
	

}
