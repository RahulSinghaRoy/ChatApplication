package com.consumer;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.ChatMessage;
import com.service.GroupChatService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaConsumerGroupService {

	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	
	@Autowired
	private GroupChatService groupChatService;
	
	@KafkaListener(topics = "group-chat-topic", groupId = "chat-group")
	public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
		String grpId = record.value();
		String keyMessage = record.key();
		//List<String> grpMember = groupChatService.getGroupDetail(grpId);
		Map<String, String> map = objectMapper.readValue(keyMessage, Map.class);
		ChatMessage chatMessage = objectMapper.readValue(keyMessage, ChatMessage.class);
		messagingTemplate.convertAndSend("/topic/group/" + grpId, chatMessage);
		
	}
}
