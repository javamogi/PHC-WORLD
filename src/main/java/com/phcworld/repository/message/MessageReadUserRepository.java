package com.phcworld.repository.message;

import com.phcworld.domain.message.MessageReadUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReadUserRepository extends JpaRepository<MessageReadUser, Long> {
}
