package com.phcworld.service.board;

import java.util.List;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;

public interface FreeBoardService {
	
	List<FreeBoard> findFreeBoardAllList();
	
	FreeBoard createFreeBoard(User user, String title, String contents, String icon);
	
	FreeBoard getOneFreeBoard(Long id);
	
	FreeBoard addFreeBoardCount(Long id);
	
	FreeBoard updateFreeBoard(FreeBoard freeBoard, String contents, String icon);
	
	void deleteFreeBoard(FreeBoard freeBoard);
	
	FreeBoard addFreeBoardAnswer(Long id);
	
	List<FreeBoard> findFreeBoardListByWriter(User loginUser);
}
