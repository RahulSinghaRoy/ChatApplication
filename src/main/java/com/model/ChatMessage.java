package com.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

	private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
    private List<FileMetadata> fileMetadataList;
}
