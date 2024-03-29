package com.phcworld.domain.image;

import com.phcworld.user.infrastructure.UserEntity;

public interface ImageService {
	Image createImage(UserEntity user, String originalName, String randName, Long size);
	
	Image findImageByRandFileName(String randName);
	
	void deleteImageById(Long id);
}
