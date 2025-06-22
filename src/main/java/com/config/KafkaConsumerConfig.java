package com.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import com.consumer.KafkaConsumerService;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
			ConsumerFactory<String, String> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		return factory;
	}

//	@Bean
//	public KafkaMessageListenerContainer<String, String> dynamicTopicListenerContainer(
//			ConsumerFactory<String, String> consumerFactory, KafkaConsumerService kafkaConsumerService) {
//
//		ContainerProperties containerProps = new ContainerProperties(Pattern.compile("chat-topic-.*"));
//		containerProps.setMessageListener((MessageListener<String, String>) kafkaConsumerService::listenRaw);
//
//		return new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
//	}
}
