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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
