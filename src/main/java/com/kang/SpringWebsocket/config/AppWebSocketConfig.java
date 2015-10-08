package com.kang.SpringWebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class AppWebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		/*  
		3-2.也就是頁面上用js來訂閱的地址，也是我們Server往Client端發送js端發送消息的地址，
		Client想要收到/topic與/queue所傳送來的訊息，必須subscribe。
		/topic與/queue 可以自己定義名字。
		詳細內容：http://goo.gl/ENNl3l
		*/
		config.enableSimpleBroker("/topic","/queue");
		//5.這裡是我的解讀，所有要傳送到Server端的訊息，似乎都要經過這個前綴，
		config.setApplicationDestinationPrefixes("/receive");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//1.雙方建立Endpoint的連線名稱，網頁上我們就可以通過這個名字連線
		registry.addEndpoint("/App").withSockJS();
	}

	@Override
	public void configureClientInboundChannel(final ChannelRegistration registration) {
	}

	@Override
	public void configureClientOutboundChannel(final ChannelRegistration registration) {
	}

}
