package com.phcworld.domain.board;

import java.util.List;

import com.phcworld.domain.user.User;

public interface FreeBoardService {
	
	List<FreeBoard> findFreeBoardAllList();
	
	FreeBoard createFreeBoard(User user, String title, String contents, String icon);
	
	FreeBoard getOneFreeBoard(Long id);
	
	FreeBoard addFreeBoardCount(Long id);
	
	FreeBoard updateFreeBoard(FreeBoard freeBoard, String contents, String icon);
	
	void deleteFreeBoardById(Long id);
	
	FreeBoard addFreeBoardAnswer(Long id);
	
	List<FreeBoard> findFreeBoardListByWriter(User loginUser);
}
