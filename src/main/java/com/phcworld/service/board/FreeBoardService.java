package com.phcworld.service.board;

import java.util.List;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.dto.FreeBoardRequest;
import com.phcworld.domain.board.dto.FreeBoardResponse;
import com.phcworld.domain.board.dto.FreeBoardSearchDto;
import com.phcworld.user.infrastructure.UserEntity;

public interface FreeBoardService {
	
	List<FreeBoardResponse> findFreeBoardAllListAndSetNewBadge();
	
	FreeBoardResponse createFreeBoard(UserEntity user, FreeBoardRequest freeBoardRequest);
	
	FreeBoardResponse getOneFreeBoard(Long id);
	
	FreeBoardResponse addFreeBoardCount(Long id);
	
	FreeBoardResponse updateFreeBoard(FreeBoardRequest freeBoardRequest);
	
	void deleteFreeBoard(Long id);
	
	List<FreeBoard> findFreeBoardListByWriter(UserEntity loginUser);

	List<FreeBoardResponse> getSearchResult(FreeBoardSearchDto search);
}
