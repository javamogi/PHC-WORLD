package com.phcworld.domain.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {

	private static final Logger log = LoggerFactory.getLogger(UserTest.class);

	private static Validator validator;
	
	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void emailWhenIsEmpty() throws Exception {
		User user = new User(null, "test", "테스트");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}

	@Test
	public void whenIsNotEmail() throws Exception {
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
	public void passwordWhenIsEmpty() throws Exception {
		User user = new User("test@test.test", null, "테스트");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	
	@Test
	public void passwordWhenIsNotSize() throws Exception {
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
	public void nameWhenIsEmpty() throws Exception {
		User user = new User("test@test.test", "test", null);
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
	@Test
	public void nameWhenIsNotSize() throws Exception {
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
	public void whenIsNotPattern() throws Exception {
		User user = new User("test@test.test", "test", "alert('dgdg')");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		assertThat(constraintViolations.size(), is(1));
		for (ConstraintViolation<User> constraintViolation : constraintViolations) {
			log.debug("violation error message : {}", constraintViolation.getMessage());
		}
	}
}
