package com.phcworld.service.board;

import java.util.List;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardResponse;
import com.phcworld.domain.user.User;

public interface FreeBoardService {
	
	List<FreeBoardResponse> findFreeBoardAllListAndSetNewBadge();
	
	FreeBoardResponse createFreeBoard(User user, FreeBoard freeBoard);
	
	FreeBoardResponse getOneFreeBoard(Long id);
	
	FreeBoardResponse addFreeBoardCount(Long id);
	
	FreeBoardResponse updateFreeBoard(FreeBoard freeBoard);
	
	void deleteFreeBoard(Long id);
	
	List<FreeBoard> findFreeBoardListByWriter(User loginUser);
}
