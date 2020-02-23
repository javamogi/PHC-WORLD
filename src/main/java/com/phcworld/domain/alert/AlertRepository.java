package com.phcworld.domain.alert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.user.User;

public interface AlertRepository extends JpaRepository<Alert, Long> {
	Page<Alert> findByPostWriter(User user, Pageable Pageable);
}
