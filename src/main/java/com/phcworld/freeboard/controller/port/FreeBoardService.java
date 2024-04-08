package com.phcworld.freeboard.controller.port;

import java.util.List;

import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.infrastructure.UserEntity;

public interface FreeBoardService {
	
	List<FreeBoardResponse> findFreeBoardAllListAndSetNewBadge();
	
	FreeBoardResponse createFreeBoard(UserEntity user, FreeBoardRequest freeBoardRequest);
	
	FreeBoardResponse getOneFreeBoard(Long id);
	
	FreeBoardResponse addFreeBoardCount(Long id);
	
	FreeBoardResponse updateFreeBoard(FreeBoardRequest freeBoardRequest);
	
	void deleteFreeBoard(Long id);
	
	List<FreeBoardEntity> findFreeBoardListByWriter(UserEntity loginUser);

	List<FreeBoardResponse> getSearchResult(FreeBoardSearchDto search);
}
