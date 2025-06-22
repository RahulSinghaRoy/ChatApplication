package com.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ChatApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.ChatMessage;
import com.model.ContactDetail;
import com.model.FileMetadata;
import com.model.MessageRepositoryModel;
import com.model.MetadataRepositoryModel;
import com.reposatory.ContactDetailRepository;
import com.reposatory.SingleMessageMetadataRepository;
import com.reposatory.SingleMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SingleChatService {

    private final ChatApplication chatApplication;
	
	@Autowired
	private ContactDetailRepository contactDetailRepository;
	
	@Autowired
	private SingleMessageRepository singleMessageRepository;
	
	@Autowired
	private SingleMessageMetadataRepository singleMessageMetadataRepository;
	

	private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${file.upload-dir}")
	private String uploadDir;
    
    public List<MessageRepositoryModel> getSavedMessagesOfSingleChat(String user1, String user2, Pageable pageable) {
    	List<MessageRepositoryModel> messageList = singleMessageRepository.findByReceiverAndSenderOrSenderAndReceiverOrderByIdDesc(user1, user2, user1, user2, pageable);
    	
    	return messageList.stream().map(x->{
    		x.setRepositoryModels(singleMessageMetadataRepository.findByMessageId(x.getId()));
    		return x;
    	}).collect(Collectors.toList());
    }

    public void saveMessageData(ChatMessage chatMessage, List<FileMetadata> list) {
    	try {
			String str = objectMapper.writeValueAsString(chatMessage);
			MessageRepositoryModel repositoryModel = objectMapper.readValue(str, MessageRepositoryModel.class);
			MessageRepositoryModel savedMessage = singleMessageRepository.save(repositoryModel);
			Long id = savedMessage.getId();
			//MetadataRepositoryModel metadataRepositoryModel =null;
			
			
			for(FileMetadata data : list) {
				String metaStr = objectMapper.writeValueAsString(data);
				MetadataRepositoryModel metadataRepositoryModel = objectMapper.readValue(metaStr, MetadataRepositoryModel.class);
				metadataRepositoryModel.setMessageId(id);
				
				singleMessageMetadataRepository.save(metadataRepositoryModel);
			}
			
			System.out.println(repositoryModel);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void sendMessage(ChatMessage chatMessage, MultipartFile[] files) {
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
			saveMessageData(chatMessage, list);
			chatMessage.setFileMetadataList(list);
    		//String topic = "chat-topic"+chatMessage.getReceiver();
    		String topic = "chat-topic";
    		String jsonMessage = objectMapper.writeValueAsString(chatMessage);
            kafkaTemplate.send(topic, jsonMessage);
    	}catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    public ContactDetail saveContactDetail(ContactDetail contactDetail) {
		return contactDetailRepository.save(contactDetail);
	}
    
   public List<ContactDetail> getContactDetail(String username){
	   List<ContactDetail> list = contactDetailRepository.findByUserName(username);
	   
	   return list;
   }
}
