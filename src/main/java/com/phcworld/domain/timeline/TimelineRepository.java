package com.phcworld.domain.timeline;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.user.User;

public interface TimelineRepository extends JpaRepository<Timeline, Long> {
	Page<Timeline> findByUser(User user, Pageable Pageable);
}
