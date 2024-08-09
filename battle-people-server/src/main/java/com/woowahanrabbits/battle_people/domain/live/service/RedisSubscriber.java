package com.woowahanrabbits.battle_people.domain.live.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahanrabbits.battle_people.domain.live.dto.RedisTopicDto;
import com.woowahanrabbits.battle_people.domain.live.dto.response.WriteChatResponseDto;

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
			String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
			String publishMessage = new String(message.getBody(), StandardCharsets.UTF_8);

			if (channel.equals("chat")) {
				RedisTopicDto<?> redisTopicDto = objectMapper.readValue(publishMessage, RedisTopicDto.class);

				Long battleBoardId = redisTopicDto.getChannelId();
				String type = redisTopicDto.getType();
				if (type.equals("chat")) {
					RedisTopicDto<WriteChatResponseDto> chatTopicDto = objectMapper.readValue(publishMessage,
						new TypeReference<>() {
						});
					WriteChatResponseDto returnValue = chatTopicDto.getResponseDto();
					messagingTemplate.convertAndSend("/topic/chat/" + battleBoardId, returnValue);
				}
			} else if (channel.equals("request")) {
				RedisTopicDto<?> redisTopicDto = objectMapper.readValue(publishMessage, RedisTopicDto.class);

				System.out.println(redisTopicDto);

				if (redisTopicDto != null) {
					Long channelId = redisTopicDto.getChannelId();
					messagingTemplate.convertAndSend("/topic/request/" + channelId, redisTopicDto.getResponseDto());
				}

			} else if (channel.equals("vote")) {
				RedisTopicDto<?> redisTopicDto = objectMapper.readValue(publishMessage, RedisTopicDto.class);
				Long battleBoardId = redisTopicDto.getChannelId();
				String type = redisTopicDto.getType();

				System.out.println(redisTopicDto.getResponseDto());

				if (type.equals("vote")) {
					RedisTopicDto<List<?>> responseTopicDto = objectMapper.readValue(publishMessage,
						new TypeReference<>() {
						});
					System.out.println(responseTopicDto.getResponseDto());
					messagingTemplate.convertAndSend("/topic/vote/" + battleBoardId,
						responseTopicDto.getResponseDto().get(1));
				}
			}

		} catch (Exception e) {
			log.error("Error processing message", e);
		}
	}
}
