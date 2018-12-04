package com.phcworld.domain.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineRepository;
import com.phcworld.domain.user.User;

@Service
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService {
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;

	@Override
	public List<FreeBoard> findFreeBoardAllList() {
		return freeBoardRepository.findAll();
	}

	@Override
	public FreeBoard createFreeBoard(User user, String title, String contents, String icon) {
		FreeBoard freeBoard = new FreeBoard(user, title, icon, contents);
		freeBoardRepository.save(freeBoard);
		
		Timeline timeline = new Timeline("free board", "list-alt", freeBoard, user, freeBoard.getCreateDate());
		timelineRepository.save(timeline);
		
		freeBoard.setTimeline(timeline);
		return freeBoardRepository.save(freeBoard);
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
	public void deleteFreeBoardById(Long id) {
		freeBoardRepository.deleteById(id);
	}
	
	@Override
	public FreeBoard addFreeBoardAnswer(Long freeboardId) {
		FreeBoard freeBoard = freeBoardRepository.getOne(freeboardId);
		freeBoard.addAnswer();
		return freeBoardRepository.save(freeBoard);
	}
	
	@Override
	public List<FreeBoard> findFreeBoardListByWriter(User loginUser) {
		return freeBoardRepository.findByWriter(loginUser);
	}

}
