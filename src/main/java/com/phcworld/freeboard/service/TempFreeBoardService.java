package com.phcworld.freeboard.service;

import java.util.List;

import com.phcworld.freeboard.infrastructure.TempFreeBoard;
import com.phcworld.freeboard.domain.dto.TempFreeBoardRequest;
import com.phcworld.freeboard.controller.port.TempFreeBoardResponse;
import com.phcworld.user.infrastructure.UserEntity;

public interface TempFreeBoardService {
	
	List<TempFreeBoardResponse> findFreeBoardAllListAndSetNewBadge();
	
	TempFreeBoardResponse createFreeBoard(UserEntity user, TempFreeBoardRequest freeBoardRequest);
	
	TempFreeBoardResponse getOneFreeBoard(Long id);
	
	TempFreeBoardResponse addFreeBoardCount(Long id);
	
	TempFreeBoardResponse updateFreeBoard(TempFreeBoardRequest freeBoardRequest);
	
	void deleteFreeBoard(Long id);
	
	List<TempFreeBoard> findFreeBoardListByWriter(UserEntity loginUser);
}
