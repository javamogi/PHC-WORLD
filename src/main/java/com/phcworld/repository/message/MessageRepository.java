package com.phcworld.repository.message;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.message.Message;
import com.phcworld.domain.user.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByReceiverOrderByIdDesc(User user);
	Page<Message> findByReceiver(User user, Pageable Pageable);
	
	List<Message> findBySenderOrderByIdDesc(User user);
	Page<Message> findBySender(User user, Pageable Pageable);
	
	List<Message> findAllByReceiverAndConfirm(User user, String confirm);
	Page<Message> findByReceiverAndConfirm(User user, String confirm, Pageable Pageable);
}
