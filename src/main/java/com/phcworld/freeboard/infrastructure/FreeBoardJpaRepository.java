package com.phcworld.freeboard.infrastructure;


import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

public interface FreeBoardJpaRepository extends JpaRepository<FreeBoardEntity, Long>, FreeBoardJpaRepositoryCustom {
	List<FreeBoardEntity> findByWriter(UserEntity user);

	@Query("select f from FreeBoardEntity f join fetch f.writer")
	List<FreeBoardEntity> findAllByFetch();
}
