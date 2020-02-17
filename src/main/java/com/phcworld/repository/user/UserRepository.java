package com.phcworld.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
