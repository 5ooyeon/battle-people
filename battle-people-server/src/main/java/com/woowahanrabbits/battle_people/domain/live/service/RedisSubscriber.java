package com.woowahanrabbits.battle_people.domain.live.service;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahanrabbits.battle_people.domain.live.dto.OpenViduTokenResponseDto;
import com.woowahanrabbits.battle_people.domain.live.dto.RedisTopicDto;
import com.woowahanrabbits.battle_people.domain.live.dto.response.WriteChatResponseDto;
import com.woowahanrabbits.battle_people.domain.live.dto.response.WriteTalkResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate messagingTemplate;
	private final MessageConverter messageConverter;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			System.out.println("Redis");
			String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
			String publishMessage = new String(message.getBody(), StandardCharsets.UTF_8);
			RedisTopicDto<?> redisTopicDto = objectMapper.readValue(publishMessage, RedisTopicDto.class);
			System.out.println(channel);
			System.out.println("topic: " + redisTopicDto);
			System.out.println(channel);
			System.out.println(redisTopicDto.getType());

			if (channel.equals("chat")) {
				Long battleBoardId = redisTopicDto.getChannelId();
				String type = redisTopicDto.getType();
				if (type.equals("chat")) {
					RedisTopicDto<WriteChatResponseDto> chatTopicDto = objectMapper.readValue(publishMessage,
						new TypeReference<>() {
						});
					WriteChatResponseDto returnValue = chatTopicDto.getResponseDto();
					messagingTemplate.convertAndSend("/topic/chat/" + battleBoardId, returnValue);
				}
			} else if (channel.equals("live")) {
				System.out.println(redisTopicDto);
				Long channelId = redisTopicDto.getChannelId();

				if (redisTopicDto.getType().equals("accept")) {
					LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>)redisTopicDto.getResponseDto();
					OpenViduTokenResponseDto dto = objectMapper.convertValue(map, OpenViduTokenResponseDto.class);
					System.out.println("accept: " + dto);
					messagingTemplate.convertAndSend("/topic/live/" + channelId + "-" + dto.getUserId(), dto);
				}
				if (redisTopicDto.getType().equals("request")) {
					LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>)redisTopicDto.getResponseDto();
					WriteTalkResponseDto returnValue = objectMapper.convertValue(map, WriteTalkResponseDto.class);
					System.out.println("request: " + returnValue);
					System.out.println("/topic/live/" + channelId);
					messagingTemplate.convertAndSend(
						"/topic/live/" + channelId, returnValue);

				}
				if (redisTopicDto.getType().equals("vote")) {
					System.out.println(redisTopicDto.getResponseDto());
					RedisTopicDto<List<?>> responseTopicDto = objectMapper.readValue(publishMessage,
						new TypeReference<>() {
						});
					System.out.println(responseTopicDto.getResponseDto());
					messagingTemplate.convertAndSend("/topic/live/" + channelId,
						responseTopicDto.getResponseDto().get(1));
				}

			}

		} catch (Exception e) {
			log.error("Error processing message", e);
		}
	}
}
