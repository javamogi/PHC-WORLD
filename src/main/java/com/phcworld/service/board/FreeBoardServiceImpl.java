package com.phcworld.service.board;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.FreeBoardRepository;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService {
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@Autowired
	private TimelineServiceImpl timelineService;

	@Override
	public List<FreeBoard> findFreeBoardAllList() {
		return freeBoardRepository.findAll();
	}

	@Override
	public FreeBoard createFreeBoard(User user, String title, String contents, String icon) {
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title(title)
				.contents(contents)
				.icon(icon)
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		
		timelineService.createTimeline(createdFreeBoard);
		
		return createdFreeBoard;
	}

	@Override
	public FreeBoard getOneFreeBoard(Long id) {
		return freeBoardRepository.getOne(id);
	}

	@Override
	public FreeBoard addFreeBoardCount(Long id) {
		FreeBoard freeBoard = freeBoardRepository.getOne(id);
		freeBoard.addCount();
		return freeBoardRepository.save(freeBoard);
	}

	@Override
	public FreeBoard updateFreeBoard(FreeBoard freeBoard, String contents, String icon) {
		freeBoard.update(contents, icon);
		return freeBoardRepository.save(freeBoard);
	}

	@Override
	public void deleteFreeBoard(FreeBoard freeBoard) {
		timelineService.deleteTimeline(freeBoard);
		freeBoardRepository.delete(freeBoard);
	}
	
	@Override
	public List<FreeBoard> findFreeBoardListByWriter(User loginUser) {
		return freeBoardRepository.findByWriter(loginUser);
	}
	
}
