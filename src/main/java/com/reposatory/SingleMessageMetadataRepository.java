package com.reposatory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.MetadataRepositoryModel;

public interface SingleMessageMetadataRepository extends JpaRepository<MetadataRepositoryModel, Long>{

	List<MetadataRepositoryModel> findByMessageId(Long id);
}
