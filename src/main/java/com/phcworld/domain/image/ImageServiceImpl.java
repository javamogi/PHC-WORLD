package com.phcworld.domain.image;

import java.time.LocalDateTime;

import com.phcworld.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
	
//	@Autowired
	private final ImageRepository imageRepository;
	
	@Override
	public Image createImage(User user, String originalName, String randName, Long size) {
//		Image image = new Image(user, originalName, randName, size);
		Image image = Image.builder()
				.writer(user)
				.originalFileName(originalName)
				.randFileName(randName)
				.size(size)
				.createDate(LocalDateTime.now())
				.build();
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
		return imageRepository.findById(id)
				.orElseThrow(NotFoundException::new);
	}
}
