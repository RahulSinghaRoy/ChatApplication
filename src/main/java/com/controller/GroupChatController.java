package com.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.model.GroupChatData;
import com.model.GroupChatMessage;
import com.service.GroupChatService;



@RestController
@RequestMapping("/groupchat")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupChatController {
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	@Autowired
	private GroupChatService groupChatService;
	
	@PostMapping("/creategroup")
	public ResponseEntity<GroupChatMessage> createGroup(@RequestBody GroupChatMessage groupChatMessage){
		groupChatMessage.setCreatedAt(LocalDateTime.now());
		GroupChatMessage chatService = groupChatService.saveGroupDetail(groupChatMessage);
		
		return new ResponseEntity<GroupChatMessage>(chatService, HttpStatus.OK);
	}

	@GetMapping("/getgrpdetail/{name}")
	public ResponseEntity<List<GroupChatData>> getGrpDetailByName(@PathVariable("name") String name){
		List<GroupChatData> list = groupChatService.findGroupDetailByName(name);
		
		return new ResponseEntity<List<GroupChatData>>(list, HttpStatus.OK);
	}
	
	@PostMapping("/sendmessage/{groupid}")
	public ResponseEntity<HttpStatus> sendGroupMessage(@PathVariable("groupid") String groupid, 
														@RequestParam("sender") String sender,
														@RequestParam(value = "message", required = false) String message,
														@RequestParam(value = "files", required = false) MultipartFile[] files){
		
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSender(sender);
		chatMessage.setContent(message);
		chatMessage.setTimestamp(LocalDateTime.now());
		
		groupChatService.sendMessage(chatMessage, groupid, files);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
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
	
	
}
