package com.reposatory;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.MessageRepositoryModel;

public interface SingleMessageRepository extends JpaRepository<MessageRepositoryModel, Long> {

	List<MessageRepositoryModel> findByReceiverAndSenderOrSenderAndReceiverOrderByIdDesc(String user1, String user2, String user3, String user4, Pageable pageable);
}
