package com.phcworld.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
	Image findByRandFileName(String randName);
}
