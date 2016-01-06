package com.kang.SpringWebsocket.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kang.SpringWebsocket.item.MessageInput;
import com.kang.SpringWebsocket.item.Result;

@Controller
public class HomeController {

	@Autowired
	private SimpMessagingTemplate template;
	
	@RequestMapping("/user")
	@ResponseBody
	  public Principal user(Principal user) {
		
	    return user;
	  }
	
	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletResponse response) throws IOException {
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "/")
	public ModelAndView test(HttpServletResponse response) throws IOException {
	
		return new ModelAndView("index");
	}
	//4-2.這裡會自動Binding 放進 叫做 input 的 instance，將結果做一些處理，最後使用SimpMessagingTemplate廣播給各個訂閱/topic/showResult的Client。
	@MessageMapping("/add")
	// @SendTo("/topic/showResult")
	public void addNum(MessageInput input,Principal principal) throws Exception {
		Result result = new Result(principal.getName() + "：" + input.getMessage());
		this.template.convertAndSend("/topic/showResult", result);
		// return result;
	}
	//5-2.這裡會廣播訂閱/queue/search的Client
	@MessageMapping("/search")
	@SendToUser("/queue/search") // <- maps to "/user/queue/search"，其實括弧內的destination其實也可以省略
	public String search(Principal principal) throws Exception{
		return "(System)Hi " + principal.getName() + " , This message only for you.";
	}
}
