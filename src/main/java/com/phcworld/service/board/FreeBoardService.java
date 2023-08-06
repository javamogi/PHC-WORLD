package com.phcworld.service.board;

import java.util.List;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardRequest;
import com.phcworld.domain.board.FreeBoardResponse;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.dto.FreeBoardSelectDto;

public interface FreeBoardService {
	
	List<FreeBoardResponse> findFreeBoardAllListAndSetNewBadge();
	
	FreeBoardResponse createFreeBoard(User user, FreeBoardRequest freeBoardRequest);
	
	FreeBoardResponse getOneFreeBoard(Long id);
	
	FreeBoardResponse addFreeBoardCount(Long id);
	
	FreeBoardResponse updateFreeBoard(FreeBoardRequest freeBoardRequest);
	
	void deleteFreeBoard(Long id);
	
	List<FreeBoard> findFreeBoardListByWriter(User loginUser);

	List<FreeBoardResponse> getByQuerydsl(int pageNum, int pageSize);
}
