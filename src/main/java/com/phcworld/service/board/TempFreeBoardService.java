package com.phcworld.service.board;

import java.util.List;

import com.phcworld.domain.board.TempFreeBoard;
import com.phcworld.domain.board.TempFreeBoardRequest;
import com.phcworld.domain.board.TempFreeBoardResponse;
import com.phcworld.domain.user.User;

public interface TempFreeBoardService {
	
	List<TempFreeBoardResponse> findFreeBoardAllListAndSetNewBadge();
	
	TempFreeBoardResponse createFreeBoard(User user, TempFreeBoardRequest freeBoardRequest);
	
	TempFreeBoardResponse getOneFreeBoard(Long id);
	
	TempFreeBoardResponse addFreeBoardCount(Long id);
	
	TempFreeBoardResponse updateFreeBoard(TempFreeBoardRequest freeBoardRequest);
	
	void deleteFreeBoard(Long id);
	
	List<TempFreeBoard> findFreeBoardListByWriter(User loginUser);
}
