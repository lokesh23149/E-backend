package com.ecom.my_ecom.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.my_ecom.services.OpenAIChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Autowired
	private OpenAIChatService chatService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> chat(@RequestBody(required = false) Map<String, Object> payload) {
		String messageText = "";
		if (payload != null && payload.get("messageText") != null) {
			messageText = payload.get("messageText").toString();
		}
		String reply = chatService.chat(messageText);
		return ResponseEntity.ok(reply != null ? reply : "");
	}
}
