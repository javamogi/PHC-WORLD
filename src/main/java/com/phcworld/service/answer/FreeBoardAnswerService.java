package com.phcworld.service.answer;

import java.util.List;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.user.User;

public interface FreeBoardAnswerService {
//	FreeBoardAnswer createFreeBoardAnswer(User loginUser, Long freeboardId, String contents);
	FreeBoardAnswerApiResponse createFreeBoardAnswer(User loginUser, Long freeboardId, String contents);
	
//	String deleteFreeBoardAnswer(Long id, User loginUser);
	SuccessResponse deleteFreeBoardAnswer(Long id, User loginUser);
	
	List<FreeBoardAnswer> findFreeBoardAnswerListByWriter(User loginUser);
}
