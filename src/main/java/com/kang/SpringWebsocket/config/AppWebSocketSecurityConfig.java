package com.kang.SpringWebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;
@EnableWebSecurity
@Configuration
public class AppWebSocketSecurityConfig
      extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    	messages
		// message types other than MESSAGE and SUBSCRIBE
        .nullDestMatcher().authenticated() 
        // anyone can subscribe to the errors
        .simpSubscribeDestMatchers("/user/queue/errors").permitAll() 
        // matches any destination that starts with /app/
        .simpDestMatchers("/receive/**").hasRole("ADMIN") 
        //3-3.
        // matches any destination for SimpMessageType.SUBSCRIBE that starts with
		// /user/ or /topic/
        .simpSubscribeDestMatchers("/user/**", "/topic/**").hasRole("ADMIN") 
        
        // (i.e. cannot send messages directly to /topic/, /queue/)
		// (i.e. cannot subscribe to /topic/messages/* to get messages sent to
		// /topic/messages-user<id>)
        .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
        // catch all
        .anyMessage().denyAll(); 
    }
    //Disable CSRF within WebSockets
    //If you want to allow other domains to access your site, you can disable Spring Security’s protection. 
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
    
}