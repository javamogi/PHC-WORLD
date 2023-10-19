package com.phcworld.repository.user;

import com.phcworld.domain.statistics.UserStatistics;
import com.phcworld.domain.statistics.UserStatisticsInterface;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	User findByEmail(String email);

	@Query(nativeQuery = true,
	value = "SELECT COUNT(*) AS count, FORMATDATETIME(USERS.CREATE_DATE, 'yyyy-MM-dd') AS date " +
			"FROM USERS " +
			"WHERE USERS.CREATE_DATE >= :startDate " +
			"AND USERS.CREATE_DATE <= :endDate " +
			"GROUP BY date")
	List<UserStatisticsInterface> findRegisterUserStatisticsForNativeQuery(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
