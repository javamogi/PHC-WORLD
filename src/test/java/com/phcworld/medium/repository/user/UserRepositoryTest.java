package com.phcworld.medium.repository.user;

import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.infrastructure.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
public class UserRepositoryTest {
	
	@Autowired
	private UserJpaRepository userRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Test
    @Ignore
	public void insert(){

		List<UserEntity> list = IntStream.range(0, 10000 * 1)
				.parallel()
				.mapToObj(i -> UserEntity.builder()
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
		UserEntity user = UserEntity.builder()
				.email("user@test.test")
				.password("user")
				.name("user")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		UserEntity newUser = userRepository.save(user);
		assertNotNull(newUser);
	}
	
	@Test
	public void selectUserByEmail() {
		createUser();
		UserEntity selectUser = userRepository.findByEmail("user@test.test").get();
		assertNotNull(selectUser);		
	}
	
	@Test
	public void updateUser() {
		createUser();
		UserEntity user = userRepository.findByEmail("user@test.test").get();
		log.info("User : {}", user);
		UserEntity updateUser = UserEntity.builder()
				.password("user1")
				.name("user1")
				.build();
		user.update(updateUser);
		UserEntity updatedUser = userRepository.save(user);
		assertEquals(updateUser.getPassword(), updatedUser.getPassword());
		assertEquals(updateUser.getName(), updatedUser.getName());
	}
	
	@Test
	public void deleteUser() {
		createUser();
		UserEntity selectUser = userRepository.findByEmail("user@test.test").get();
		userRepository.delete(selectUser);
		UserEntity deletedUser = userRepository.findByEmail("user@test.test").get();
		assertNull(deletedUser);
	}
}
