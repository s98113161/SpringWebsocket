package com.kang.SpringWebsocket.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class RandomDataGenerator implements ApplicationListener<BrokerAvailabilityEvent> {

	private final SimpMessagingTemplate messagingTemplate;

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	@Autowired
	public RandomDataGenerator(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onApplicationEvent(final BrokerAvailabilityEvent event) {
	}

	@Scheduled(fixedDelay = 2000)
	public void sendDataToUsers() {
		List<Object> principals = sessionRegistry.getAllPrincipals();
		List<String> usersNamesList = new ArrayList<String>();
		for (Object principal : principals) {
			if (principal instanceof User) {
				usersNamesList.add(((User) principal).getUsername());
				this.messagingTemplate.convertAndSendToUser(((User) principal).getUsername(), "/queue/chart",
						String.valueOf(new Random().nextInt(100)));
			}
		}
	}
}