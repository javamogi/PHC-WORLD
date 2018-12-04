package com.phcworld.domain.answer;

import java.util.List;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;

public interface FreeBoardAnswerService {
	FreeBoardAnswer createFreeBoardAnswer(User loginUser, FreeBoard freeBoard, String contents);
	
	String deleteFreeBoardAnswer(Long id, User loginUser, Long freeboardId);
	
	List<FreeBoardAnswer> findFreeBoardAnswerListByWriter(User loginUser);
}
