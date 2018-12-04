package com.phcworld.domain.good;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.user.User;

public interface GoodRepository extends JpaRepository<Good, Long> {
	Page<Good> findByUser(User user, Pageable Pageable);
}
