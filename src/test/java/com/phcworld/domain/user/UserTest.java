package com.phcworld.domain.user;

import static org.hamcrest.CoreMatchers.is;
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
		log.debug("User : {}", user);
	}

	@Test
	public void isEmptyEmail() throws Exception {
		User user = new User(null, "test", "테스트");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}

	@Test
	public void isNotEmail() throws Exception {
		User user = new User("test", "test", "테스트");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}

	@Test
	public void matchPassword() throws Exception {
		User user = new User("test@test.test", "test", "테스트");
		assertTrue(user.matchPassword("test"));
		assertFalse(user.matchPassword("pass"));
	}
	
	@Test
	public void isEmptyPassword() throws Exception {
		User user = new User("test@test.test", null, "테스트");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void isNotSizePassword() throws Exception {
		User shortUser = new User("test@test.tet", "tes", "테스트");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(shortUser);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
//	@Test
//	public void matchId() throws Exception {
//		User user = new User("test@test.test", "test", "테스트");
//		assertTrue(user.matchId(1L));
//		assertFalse(user.matchId(2L));
//	}

	@Test
	public void isEmptyName() throws Exception {
		User user = new User("test@test.test", "test", null);
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	@Test
	public void isNotSizeName() throws Exception {
		User shortUser = new User("test@test.test", "test", "테");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(shortUser);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		
		User longUser = new User("test@test.test", "test", "테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트");
		constraintViolations = validator.validate(longUser);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
		
	}
	
	@Test
	public void isNotPasswordPattern() throws Exception {
		User user = new User("test@test.test", "test", "alert('dgdg')");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
}
