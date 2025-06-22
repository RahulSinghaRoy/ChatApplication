package com.reposatory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.ContactDetail;

public interface ContactDetailRepository extends JpaRepository<ContactDetail, Long> {

	List<ContactDetail> findByUserName(String name);
}
