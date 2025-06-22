package com.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.ChatMessage;
import com.model.ContactDetail;
import com.model.FileMetadata;
import com.model.GroupChatData;
import com.model.GroupChatMessage;
import com.reposatory.ContactDetailRepository;
import com.reposatory.GroupChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupChatService {
	@Autowired
	private GroupChatRepository groupChatRepository;
	
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public GroupChatMessage saveGroupDetail(GroupChatMessage chatMessage) {
		return groupChatRepository.save(chatMessage);
	}

	public List<GroupChatData> findGroupDetailByName(String name) {
		List<GroupChatData> list = groupChatRepository.findGroupdetailByName(name);
		return list;
	}

	public List<String> getGroupDetail(String groupId) {
		List<String> list = groupChatRepository.findGroupMember(groupId);
		return list;
	}

	public void sendMessage(ChatMessage chatMessage, String groupId, MultipartFile[] files) {
		try {
			List<FileMetadata> list = new ArrayList<>();
			FileMetadata fileMetadata = null;

			if (files != null) {
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						fileMetadata = new FileMetadata();
						String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						Path uploadPath = Paths.get(uploadDir);
						if (!Files.exists(uploadPath)) {
							Files.createDirectories(uploadPath);
						}
						Path filePath = uploadPath.resolve(fileName);
						Files.write(filePath, file.getBytes());

						fileMetadata.setFileName(file.getOriginalFilename());
						fileMetadata.setFileType(file.getContentType());
						fileMetadata.setFileUrl("/files/uploads/" + fileName);

						list.add(fileMetadata);
					}
				}
			}
			chatMessage.setFileMetadataList(list);
			String topic = "group-chat-topic";
			String jsonMessage = objectMapper.writeValueAsString(chatMessage);
			kafkaTemplate.send(topic, jsonMessage, groupId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
}
