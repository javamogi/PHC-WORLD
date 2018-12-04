package com.phcworld.domain.image;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.phcworld.domain.user.User;

@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String originalFileName;

	private String randFileName;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_image_writer"))
	private User writer;

	private Long size;

	private LocalDateTime createDate;

	public Image() {
	}

	public Image(User writer, String originalFileName, String randFileName, Long size) {
		this.writer = writer;
		this.originalFileName = originalFileName;
		this.randFileName = randFileName;
		this.size = size;
		this.createDate = LocalDateTime.now();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public String getRandFileName() {
		return randFileName;
	}

	public User getWriter() {
		return writer;
	}

	public Long getSize() {
		return size;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	@Override
	public String toString() {
		return "Image [id=" + id + ", originalFileName=" + originalFileName + ", randFileName=" + randFileName
				+ ", writer=" + writer + ", size=" + size + ", createDate=" + createDate + "]";
	}

}
