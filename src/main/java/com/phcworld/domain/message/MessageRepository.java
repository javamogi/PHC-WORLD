package com.phcworld.domain.message;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.user.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByToUserOrderByIdDesc(User user);
	Page<Message> findByToUser(User user, Pageable Pageable);
	
	List<Message> findByFromUserOrderByIdDesc(User user);
	Page<Message> findByFromUser(User user, Pageable Pageable);
	
	List<Message> findAllByToUserAndConfirm(User user, String confirm);
	Page<Message> findByToUserAndConfirm(User user, String confirm, Pageable Pageable);
}
