package com.phcworld.medium.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class UserTest {

	private static Validator validator;
	
	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test
	public void testLombok() {
		UserEntity user = UserEntity.builder()
				.email("lombok@test.test")
				.password("lombokpss")
				.name("lombok")
				.authority(Authority.ROLE_USER)
				.build();
		UserEntity test = UserEntity.builder()
				.email("lombok@test.test")
				.password("lombokpss")
				.name("lombok")
				.authority(Authority.ROLE_USER)
				.build();
		assertThat(user)
				.isEqualTo(test);
	}

	@Test
	public void createWhenIsEmptyEmail() throws Exception {
		UserEntity user = UserEntity.builder()
				.password("test")
				.name("테스트")
				.build();
		Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(user);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		assertThat(constraintViolations.size()).isEqualTo(1);
	}

	@Test
	public void createWhenIsNotEmail() throws Exception {
		UserEntity user = UserEntity.builder()
				.email("test")
				.password("test")
				.name("테스트")
				.build();
		Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size()).isEqualTo(1);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void matchId() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.build();
		assertTrue(user.matchId(1L));
		assertFalse(user.matchId(2L));
	}

	@Test
	public void matchPassword() throws Exception {
		UserEntity user = UserEntity.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.build();
		assertTrue(user.matchPassword("test"));
		assertFalse(user.matchPassword("pass"));
	}
	
	@Test
	public void createWhenIsEmptyPassword() throws Exception {
		UserEntity user = UserEntity.builder()
				.email("test@test.test")
				.password(null)
				.name("테스트")
				.build();
		Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(user);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
	@Test
	public void createWhenIsNotSizePassword() throws Exception {
		UserEntity user = UserEntity.builder()
				.email("test@test.test")
				.password("tes")
				.name("테스트")
				.build();
		Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size()).isEqualTo(1);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void createWhenIsEmptyName() throws Exception {
		UserEntity user = UserEntity.builder()
				.email("test@test.test")
				.password("test")
				.name(null)
				.build();
		Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(user);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	@Test
	public void createWhenIsNotSizeName() throws Exception {
		UserEntity shortNameUser = UserEntity.builder()
				.email("test@test.test")
				.password("test")
				.name("테")
				.build();
		Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(shortNameUser);
		assertThat(constraintViolations.size()).isEqualTo(1);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		
		UserEntity longNameUser = UserEntity.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트")
				.build(); 
		constraintViolations = validator.validate(longNameUser);
		assertThat(constraintViolations.size()).isEqualTo(1);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		
	}
	
	@Test
	public void createWhenIsNotPasswordPattern() throws Exception {
		UserEntity user = UserEntity.builder()
				.email("test@test.test")
				.password("test")
				.name("alert('dgdg')")
				.build();
		Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size()).isEqualTo(1);
		for (ConstraintViolation<UserEntity> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void matchAdminAuthority() {
		UserEntity adminUser = UserEntity.builder()
				.authority(Authority.ROLE_ADMIN)
				.build();
		assertTrue(adminUser.matchAdminAuthority());
		
		UserEntity user = UserEntity.builder()
				.authority(Authority.ROLE_USER)
				.build();
		assertFalse(user.matchAdminAuthority());
	}
	
	@Test
	public void ifMeSetAdmin() {
		UserEntity me = UserEntity.builder()
				.email("pakoh200@naver.com")
				.build();
		me.ifMeSetAdmin();
		assertThat(me.getAuthority()).isEqualTo(Authority.ROLE_ADMIN);
	}
}
