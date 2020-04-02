package com.phcworld.service.board;

import java.util.List;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;

public interface FreeBoardService {
	
	List<FreeBoard> findFreeBoardAllListAndSetNewBadge();
	
	FreeBoard createFreeBoard(User user, FreeBoard freeBoard);
	
	FreeBoard getOneFreeBoard(Long id);
	
	FreeBoard addFreeBoardCount(Long id);
	
	FreeBoard updateFreeBoard(FreeBoard freeBoard);
	
	void deleteFreeBoard(FreeBoard freeBoard);
	
	List<FreeBoard> findFreeBoardListByWriter(User loginUser);
}
