package com.model;

import java.time.LocalDateTime;
import java.util.List;



import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatMessage {

	@Id
	private String id;
    private String groupName;
    private String creator;
    private LocalDateTime createdAt;
    @ElementCollection
    @CollectionTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id")
    )
    @Column(name = "member")
    private List<String> members;
}
