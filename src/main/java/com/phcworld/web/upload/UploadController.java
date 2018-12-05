package com.phcworld.web.upload;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phcworld.domain.image.Image;
import com.phcworld.domain.image.ImageServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.domain.user.UserService;
import com.phcworld.web.HttpSessionUtils;

@RestController
@RequestMapping("/upload")
public class UploadController {
	
	private static final Logger log = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private ImageServiceImpl imageService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/imageUpload")
	public Image create(@RequestParam("imageFile") MultipartFile multipartFile, HttpServletRequest request, HttpSession session, Exception exception) {
		Long size = null;
		String originalName = "", fileExtension = "", randName = "";
		if(multipartFile != null) {
			size = multipartFile.getSize();
			originalName = multipartFile.getOriginalFilename();
			fileExtension = originalName.substring(originalName.lastIndexOf("."));
			log.debug("fileExtension : {}", fileExtension);
			randName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
		}
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);

		Path path = Paths.get("");
		String pathStr = path.toAbsolutePath().toString();
		
		log.debug("pathStr : {}", pathStr);
		
		String realPath = request.getSession().getServletContext().getRealPath("/");
		log.debug("realPath : {}", realPath);
		
		try {
//			multipartFile.transferTo(new File(pathStr + File.separator + "src" + File.separator + "main" + File.separator + "resources"+ File.separator +"static"+ File.separator +"images" + File.separator + randName));
			// 로컬환경
			multipartFile.transferTo(new File(realPath + File.separator + "webapps" + File.separator + "ROOT" + File.separator + "WEB-INF"+ File.separator +"classes"+ File.separator +"static" + File.separator + "images" + File.separator + randName));
		} catch(Exception e) {
			log.debug("Error : {}", e);;
		}
		
		return imageService.createImage(sessionedUser, originalName, randName, size);
	}
	
	@GetMapping("/deleteImage")
	public String delete(@RequestParam("randFileName") String randName) {
		Image img = imageService.findImageByRandFileName(randName);
		if(img != null) {
			imageService.deleteImageById(img.getId());
		}
		Path path = Paths.get("");
		String pathStr = path.toAbsolutePath().toString();
		File deleteFile = new File(pathStr + File.separator + "src" + File.separator + "main" + File.separator + "resources"+ File.separator +"static"+ File.separator +"images" + File.separator + randName);
		if(deleteFile.exists()) {
			if(deleteFile.delete()) {
				log.debug("delete success");
			}
		}
		return "{\"success\":\"success\"}";
	}
	
	@GetMapping("/findImage")
	public Image find(@RequestParam("randFileName") String randName) {
		log.debug("randName : {}", randName); 
		Image img = imageService.findImageByRandFileName(randName);
		return img;
	}
	
	@PostMapping("/profileUpload")
	public User profileUpload(@RequestParam("profileImage") MultipartFile multipartFile, HttpSession session, Exception exception) {
		String originalName = multipartFile.getOriginalFilename();
		String fileExtension = originalName.substring(originalName.lastIndexOf("."));
		String randName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);

		Path path = Paths.get("");
		String pathStr = path.toAbsolutePath().toString();
		
		try {
			multipartFile.transferTo(new File(pathStr + File.separator + "src" + File.separator + "main" + File.separator + "resources"+ File.separator +"static"+ File.separator +"images" + File.separator +"profile" + File.separator + randName));
		} catch(Exception e) {
			log.debug("Error : {}", e);;
		}
		
		return userService.imageUpdate(sessionedUser, randName);
	}
}
