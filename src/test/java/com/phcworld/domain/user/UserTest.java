package com.phcworld.domain.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
		User user = User.builder()
				.email("lombok@test.test")
				.password("lombokpss")
				.name("lombok")
				.authority("ROLE_USER")
				.build();
		User test = User.builder()
				.email("lombok@test.test")
				.password("lombokpss")
				.name("lombok")
				.authority("ROLE_USER")
				.build();
		assertThat(user, is(test));
	}

	@Test
	public void createWhenIsEmptyEmail() throws Exception {
		User user = User.builder()
				.password("test")
				.name("테스트")
				.build();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}

	@Test
	public void createWhenIsNotEmail() throws Exception {
		User user = User.builder()
				.email("test")
				.password("test")
				.name("테스트")
				.build();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void matchId() throws Exception {
		User user = User.builder()
				.id(1L)
				.build();
		assertTrue(user.matchId(1L));
		assertFalse(user.matchId(2L));
	}

	@Test
	public void matchPassword() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.build();
		assertTrue(user.matchPassword("test"));
		assertFalse(user.matchPassword("pass"));
	}
	
	@Test
	public void createWhenIsEmptyPassword() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password(null)
				.name("테스트")
				.build();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void createWhenIsNotSizePassword() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("tes")
				.name("테스트")
				.build();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void createWhenIsEmptyName() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name(null)
				.build();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	@Test
	public void createWhenIsNotSizeName() throws Exception {
		User shortNameUser = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테")
				.build();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(shortNameUser);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		
		User longNameUser = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트")
				.build(); 
		constraintViolations = validator.validate(longNameUser);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		
	}
	
	@Test
	public void createWhenIsNotPasswordPattern() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("alert('dgdg')")
				.build();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void matchAdminAuthority() {
		User adminUser = User.builder()
				.authority("ROLE_ADMIN")
				.build();
		assertTrue(adminUser.matchAdminAuthority());
		
		User user = User.builder()
				.authority("ROLE_USER")
				.build();
		assertFalse(user.matchAdminAuthority());
	}
	
	@Test
	public void ifMeSetAdmin() {
		User me = User.builder()
				.email("pakoh200@naver.com")
				.build();
		me.ifMeSetAdmin(me);
		assertThat(me.getAuthority(), is("ROLE_ADMIN"));
	}
}
