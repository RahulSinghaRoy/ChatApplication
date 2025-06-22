package com.reposatory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.GroupChatData;
import com.model.GroupChatMessage;

public interface GroupChatRepository extends JpaRepository<GroupChatMessage, String> {

	@Query(value ="select gm.group_id, gcm.creator, gcm.group_name from group_members as gm, group_chat_message as gcm where gm.group_id=gcm.id and gm.member=?1", nativeQuery = true)
	public List<GroupChatData> findGroupdetailByName(String name);
	
	@Query(value="select member from group_members where group_id=?1", nativeQuery = true)
	public List<String> findGroupMember(String id);
}
