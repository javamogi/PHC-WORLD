package com.phcworld.service.board;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardRequest;
import com.phcworld.domain.board.FreeBoardResponse;
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
	public List<FreeBoardResponse> findFreeBoardAllListAndSetNewBadge() {
		int hourOfDay = 24;
		int minutesOfHour = 60;
		List<FreeBoard> list = freeBoardRepository.findAll();
		for (int i = list.size()-1; i >= 0; i--) {
			long createdDateAndNowDifferenceMinutes = 
					Duration.between(list.get(i).getUpdateDate(), LocalDateTime.now()).toMinutes();
			if (createdDateAndNowDifferenceMinutes / minutesOfHour < hourOfDay) {
				list.get(i).setBadge("New");
			} else {
				break;
			}
		}
		List<FreeBoardResponse> freeBoardAnswerApiResponseList = list.stream()
				.map(freeBoard -> {
					FreeBoardResponse freeBoardResponse = FreeBoardResponse.builder()
							.id(freeBoard.getId())
							.writer(freeBoard.getWriter())
							.title(freeBoard.getTitle())
							.contents(freeBoard.getContents())
							.icon(freeBoard.getIcon())
							.badge(freeBoard.getBadge())
							.updateDate(freeBoard.getFormattedUpdateDate())
							.count(freeBoard.getCount())
							.countOfAnswer(freeBoard.getCountOfAnswer())
							.build();
					return freeBoardResponse;
				})
				.collect(Collectors.toList());
		return freeBoardAnswerApiResponseList;
	}

	@Override
	public FreeBoardResponse createFreeBoard(User user, FreeBoardRequest request) {
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.icon(request.getIcon())
				.count(0)
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		
		timelineService.createTimeline(createdFreeBoard);
		
		return response(createdFreeBoard);
	}

	@Override
	public FreeBoardResponse getOneFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.getOne(id);
		return response(freeBoard);
	}

	@Override
	public FreeBoardResponse addFreeBoardCount(Long id) {
		FreeBoard freeBoard = freeBoardRepository.getOne(id);
		freeBoard.addCount();
		return response(freeBoardRepository.save(freeBoard));
	}

	@Override
	public FreeBoardResponse updateFreeBoard(FreeBoardRequest request) {
		FreeBoard freeBoard = freeBoardRepository.getOne(request.getId());
		freeBoard.update(request);
		return response(freeBoardRepository.save(freeBoard));
	}

	@Override
	public void deleteFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.getOne(id);
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
	
	private FreeBoardResponse response(FreeBoard freeBoard) {
		FreeBoardResponse freeBoardResponse = FreeBoardResponse.builder()
				.id(freeBoard.getId())
				.writer(freeBoard.getWriter())
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.icon(freeBoard.getIcon())
				.badge(freeBoard.getBadge())
				.updateDate(freeBoard.getFormattedUpdateDate())
				.count(freeBoard.getCount())
				.countOfAnswer(freeBoard.getCountOfAnswer())
				.build();
		List<FreeBoardAnswer> answerList = freeBoard.getFreeBoardAnswers();
		if(answerList != null) {
			List<FreeBoardAnswerApiResponse> freeBoardAnswerApiResponseList = answerList.stream()
					.map(answer -> {
						FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
								.id(answer.getId())
								.contents(answer.getContents())
								.countOfAnswers(answer.getFreeBoard().getCountOfAnswer())
								.freeBoardId(answer.getFreeBoard().getId())
								.writer(answer.getWriter())
								.updateDate(answer.getFormattedUpdateDate())
								.build();
						return freeBoardAnswerApiResponse;
					})
					.collect(Collectors.toList());
			freeBoardResponse.setFreeBoardAnswerList(freeBoardAnswerApiResponseList);
		}
		return freeBoardResponse;
	}
	
}
