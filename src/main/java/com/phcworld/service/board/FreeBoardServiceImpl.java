package com.phcworld.service.board;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.FreeBoardRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService {
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private AlertServiceImpl alertService;

	@Override
	public List<FreeBoard> findFreeBoardAllListAndSetNewBadge() {
		int hourOfDay = 24;
		int minutesOfHour = 60;
		List<FreeBoard> list = freeBoardRepository.findAll();
		for (int i = list.size()-1; i >= 0; i--) {
			long createdDateAndNowDifferenceMinutes = 
					Duration.between(list.get(i).getCreateDate(), LocalDateTime.now()).toMinutes();
			if (createdDateAndNowDifferenceMinutes / minutesOfHour < hourOfDay) {
				list.get(i).setBadge("New");
			} else {
				break;
			}
		}
		return list;
	}

	@Override
	public FreeBoard createFreeBoard(User user, FreeBoard inputFreeBoard) {
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title(inputFreeBoard.getTitle())
				.contents(inputFreeBoard.getContents())
				.icon(inputFreeBoard.getIcon())
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
	public FreeBoard updateFreeBoard(FreeBoard newFreeBoard) {
		FreeBoard freeBoard = freeBoardRepository.getOne(newFreeBoard.getId());
		freeBoard.update(newFreeBoard);
		return freeBoardRepository.save(freeBoard);
	}

	@Override
	public void deleteFreeBoard(FreeBoard freeBoard) {
		timelineService.deleteTimeline(freeBoard);
		List<FreeBoardAnswer> answerList = freeBoard.getFreeBoardAnswers();
		for(int i = 0; i < answerList.size(); i++) {
			FreeBoardAnswer freeBoardAnswer = answerList.get(i);
			timelineService.deleteTimeline(freeBoardAnswer);
			alertService.deleteAlert(freeBoardAnswer);
		}
		freeBoardRepository.delete(freeBoard);
	}
	
	@Override
	public List<FreeBoard> findFreeBoardListByWriter(User loginUser) {
		return freeBoardRepository.findByWriter(loginUser);
	}
	
}
