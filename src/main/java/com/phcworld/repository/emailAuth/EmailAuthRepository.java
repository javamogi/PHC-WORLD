package com.phcworld.repository.emailAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.emailAuth.EmailAuth;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
	EmailAuth findByEmail(String email);
}
