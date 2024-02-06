package com.phcworld.repository.user;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryHashtag;
import com.phcworld.domain.board.Hashtag;
import com.phcworld.domain.user.Authority;
import com.phcworld.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jeasy.random.FieldPredicates.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Test
//    @Ignore
	public void insert(){

		List<User> list = IntStream.range(0, 10000 * 1)
				.parallel()
				.mapToObj(i -> User.builder()
						.email(UUID.randomUUID().toString().substring(0, 7))
						.password(UUID.randomUUID().toString().substring(0, 10))
						.name(UUID.randomUUID().toString().substring(0, 4))
						.profileImage("blank-profile-picture.png")
//						.authority(Authority.ROLE_USER)
						.build())
				.collect(Collectors.toList());

		String sql = String.format("INSERT INTO %s (email, password, name, profile_image) " +
				"VALUES (:email, :password, :name, :profileImage)", "users");

		SqlParameterSource[] params = list
				.stream()
				.map(BeanPropertySqlParameterSource::new)
				.toArray(SqlParameterSource[]::new);
		namedParameterJdbcTemplate.batchUpdate(sql, params);

	}

	@Test
	public void createUser() {
		User user = User.builder()
				.email("user@test.test")
				.password("user")
				.name("user")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		User newUser = userRepository.save(user);
		assertNotNull(newUser);
	}
	
	@Test
	public void selectUserByEmail() {
		createUser();
		User selectUser = userRepository.findByEmail("user@test.test");
		assertNotNull(selectUser);		
	}
	
	@Test
	public void updateUser() {
		createUser();
		User user = userRepository.findByEmail("user@test.test");
		log.info("User : {}", user);
		User updateUser = User.builder()
				.password("user1")
				.name("user1")
				.build();
		user.update(updateUser);
		User updatedUser = userRepository.save(user);
		assertEquals(updateUser.getPassword(), updatedUser.getPassword());
		assertEquals(updateUser.getName(), updatedUser.getName());
	}
	
	@Test
	public void deleteUser() {
		createUser();
		User selectUser = userRepository.findByEmail("user@test.test");
		userRepository.delete(selectUser);
		User deletedUser = userRepository.findByEmail("user@test.test");
		assertNull(deletedUser);
	}
}
