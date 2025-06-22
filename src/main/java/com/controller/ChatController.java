package com.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.model.ChatMessage;
import com.model.ContactDetail;
import com.model.MessageRepositoryModel;
import com.service.SingleChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

	private final SingleChatService singleChatService;
	private final SimpMessagingTemplate messagingTemplate;
	
	@Value("${file.upload-dir}")
    private String uploadDir;

	@PostMapping("/send")
	public ResponseEntity<String> sendMessage(@RequestParam(value = "sender") String sender, @RequestParam(value = "receiver") String receiver,
											   @RequestParam(value = "content") String content,
											  @RequestParam(value = "files", required = false) MultipartFile[] files) {
		
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setContent(content);
		chatMessage.setReceiver(receiver);
		chatMessage.setSender(sender);
		chatMessage.setTimestamp(LocalDateTime.now());
		
		singleChatService.sendMessage(chatMessage, files);
		return ResponseEntity.ok("Message sent");
	}
	
	@GetMapping("/files/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Path path = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }
        String contentType = Files.probeContentType(path);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
            .body(resource);
    }
	
	@PostMapping("/savecontact")
	public ResponseEntity<ContactDetail> saveContactDetail(@RequestBody ContactDetail contactDetail){
		ContactDetail detail = singleChatService.saveContactDetail(contactDetail);
		
		return new ResponseEntity<ContactDetail>(detail, HttpStatus.OK);
	}
	
	@GetMapping("/getcontactdetail/{username}")
	public ResponseEntity<List<ContactDetail>> getContactDetail(@PathVariable("username") String username){
		List<ContactDetail> contactList = singleChatService.getContactDetail(username);
		
		return new ResponseEntity<List<ContactDetail>>(contactList, HttpStatus.OK);
	}
	
	@GetMapping("/getmessages/{user1}/{user2}/{pageNumber}/{pageSize}")
	public ResponseEntity<List<MessageRepositoryModel>> getSavedMessagesOfSingleChat(@PathVariable("user1") String user1,
																					 @PathVariable("user2") String user2,
																					 @PathVariable("pageNumber") int pageNumber,
																					 @PathVariable("pageSize") int pageSize){
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<MessageRepositoryModel> messageList = singleChatService.getSavedMessagesOfSingleChat(user1, user2, pageable);
		
		return new ResponseEntity<List<MessageRepositoryModel>>(messageList, HttpStatus.OK);
	}

}
