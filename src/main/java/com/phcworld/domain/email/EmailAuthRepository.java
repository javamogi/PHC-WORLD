package com.phcworld.domain.email;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
	EmailAuth findByEmail(String email);
}
