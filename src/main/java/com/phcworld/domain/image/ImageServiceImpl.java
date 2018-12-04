package com.phcworld.domain.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Override
	public Image createImage(User user, String originalName, String randName, Long size) {
		Image image = new Image(user, originalName, randName, size);
		return imageRepository.save(image);
	}
	
	@Override
	public Image findImageByRandFileName(String randName) {
		return imageRepository.findByRandFileName(randName);
	}
	
	@Override
	public void deleteImageById(Long id) {
		imageRepository.deleteById(id);
	}

	public Image getOneImage(Long id) {
		return imageRepository.getOne(id);
	}
}
