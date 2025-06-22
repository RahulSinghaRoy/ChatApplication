package com.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.ChatMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;

//    @KafkaListener(topicPattern = "chat-topic-*", groupId = "chat-group")
//    public void listen(String message) throws JsonProcessingException {
//    	//System.out.println("Message from consumer "+ message);
//        ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
//        System.out.println("Sending message to: /topic/user/" + chatMessage.getReceiver());
//
//        messagingTemplate.convertAndSend("/topic/user/" + chatMessage.getReceiver(), chatMessage);
//    }

//    public void listenRaw(ConsumerRecord<String, String> record) {
//        try {
//            String message = record.value();
//            ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
//            String topic = record.topic();
//            System.out.println("Received on topic: " + topic + " Message: " + message);
//
//            messagingTemplate.convertAndSend("/topic/user/" + chatMessage.getReceiver(), chatMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

	@KafkaListener(topics = "chat-topic", groupId = "chat-group")
	public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
		String message = record.value();
		Map<String, String> map = objectMapper.readValue(message, Map.class);
		System.out.println("The message is : "+map.get("receiver"));
		ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
		messagingTemplate.convertAndSend("/topic/user/" + map.get("receiver"), chatMessage);
		messagingTemplate.convertAndSend("/topic/user/" + map.get("sender"), chatMessage);
	}

}
