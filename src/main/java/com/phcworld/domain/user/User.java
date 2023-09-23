package com.phcworld.domain.user;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phcworld.domain.board.TempDiary;
import com.phcworld.utils.LocalDateTimeUtils;
import com.phcworld.utils.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = "password")
@Table(name = "USERS")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email(message = "이메일 형식이 아닙니다.")
	@NotNull
	@Column(nullable = false, unique = true)
	private String email;

	@NotNull
	// @Size(min = 4, max = 12, message = "4자 이상 12자 이하로 해야합니다.")
	@Size(min = 4, message = "4자 이상으로 해야합니다.")
	@JsonIgnore
	private String password;

	@NotNull
	@Size(min = 3, max = 20, message = "영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "한글, 영문, 숫자만 가능합니다.")
	private String name;

	private String authority;

	private LocalDateTime createDate;

	private String profileImage;
	
//	@OneToMany(mappedBy = "user")
//	@JsonIgnore
//	private List<Good> goods;

	public boolean matchId(Long newId) {
		if (newId == null) {
			return false;
		}
		return newId.equals(id);
	}

	public boolean matchPassword(String inputPassword) throws NoSuchAlgorithmException {
		if (inputPassword == null) {
			return false;
		}
		if (inputPassword.equals("test") || inputPassword.equals("test2")) {
			return inputPassword.equals(this.password);
		}
		String password = SecurityUtils.getEncSHA256(inputPassword);
		return password.equals(this.password);
	}

	public void update(User updateUser) {
		this.password = updateUser.password;
		this.name = updateUser.name;
	}

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public boolean matchAdminAuthority() {
		if (this.authority == null) {
			return false;
		}
		return this.authority.equals("ROLE_ADMIN");
	}

	public void ifMeSetAdmin() {
//		if (user.getEmail().equals("pakoh200@naver.com")) {
		if (this.email.equals("pakoh200@naver.com")) {
//			user.setAuthority("ROLE_ADMIN");
			this.authority = "ROLE_ADMIN";
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(authority, user.authority);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, name, authority);
	}
}
