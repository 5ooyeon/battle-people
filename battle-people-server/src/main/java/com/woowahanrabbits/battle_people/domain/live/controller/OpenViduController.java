package com.woowahanrabbits.battle_people.domain.live.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.woowahanrabbits.battle_people.domain.live.service.OpenViduService;

import io.openvidu.java.client.OpenViduRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/openvidu")
public class OpenViduController {
	private OpenViduService openViduService;

	@PostMapping("/create-session")
	public String createSession() {
		try {
			return openViduService.createSession();
		} catch (Exception e) {
			return null;
		}

	}

	@PostMapping("/get-token")
	public String getToken(@RequestParam String roomId, @RequestParam String role) {
		try {
			OpenViduRole openViduRole = "broadcaster".equals(role) ? OpenViduRole.PUBLISHER : OpenViduRole.SUBSCRIBER;
			return openViduService.getToken(roomId, openViduRole);
		} catch (Exception e) {
			return null;
		}
	}
}
