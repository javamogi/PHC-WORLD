package com.phcworld.repository.message;

import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByReceiverOrderByIdDesc(UserEntity user);
	Page<Message> findByReceiver(UserEntity user, Pageable Pageable);
	
	List<Message> findBySenderOrderByIdDesc(UserEntity user);
	Page<Message> findBySender(UserEntity user, Pageable Pageable);
	
	List<Message> findAllByReceiverAndConfirm(UserEntity user, String confirm);
	Page<Message> findByReceiverAndConfirm(UserEntity user, String confirm, Pageable Pageable);
}
