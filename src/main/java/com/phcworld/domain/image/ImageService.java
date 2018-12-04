package com.phcworld.domain.image;

import com.phcworld.domain.user.User;

public interface ImageService {
	Image createImage(User user, String originalName, String randName, Long size);
	
	Image findImageByRandFileName(String randName);
	
	void deleteImageById(Long id);
}
