package com.phcworld.user.infrastructure;

import com.phcworld.domain.statistics.UserStatisticsInterface;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);

	@Query(nativeQuery = true,
	value = "SELECT COUNT(*) AS count, FORMATDATETIME(USERS.CREATE_DATE, 'yyyy-MM-dd') AS date " +
			"FROM USERS " +
			"WHERE USERS.CREATE_DATE >= :startDate " +
			"AND USERS.CREATE_DATE <= :endDate " +
			"GROUP BY date")
	List<UserStatisticsInterface> findRegisterUserStatisticsForNativeQuery(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
