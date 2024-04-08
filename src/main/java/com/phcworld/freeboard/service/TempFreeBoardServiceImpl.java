package com.phcworld.freeboard.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.TempFreeBoardAnswer;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.freeboard.infrastructure.TempFreeBoard;
import com.phcworld.freeboard.domain.dto.TempFreeBoardRequest;
import com.phcworld.freeboard.controller.port.TempFreeBoardResponse;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.freeboard.infrastructure.TempFreeBoardRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
@RequiredArgsConstructor
public class TempFreeBoardServiceImpl implements TempFreeBoardService {
	private final TempFreeBoardRepository freeBoardRepository;
	
	private final TimelineServiceImpl timelineService;
	
	private final AlertServiceImpl alertService;

	@Override
	public List<TempFreeBoardResponse> findFreeBoardAllListAndSetNewBadge() {
		int hourOfDay = 24;
		int minutesOfHour = 60;
		List<TempFreeBoard> list = freeBoardRepository.findAll();
		for (int i = list.size()-1; i >= 0; i--) {
			long createdDateAndNowDifferenceMinutes = 
					Duration.between(list.get(i).getCreateDate(), LocalDateTime.now()).toMinutes();
			if (createdDateAndNowDifferenceMinutes / minutesOfHour < hourOfDay) {
				list.get(i).setBadge("New");
			} else {
				if(list.get(i).getBadge().equals("")) {
					break;
				}
				list.get(i).setBadge("");
			}
		}
		List<TempFreeBoardResponse> freeBoardAnswerApiResponseList = list.stream()
				.map(freeBoard -> {
					TempFreeBoardResponse freeBoardResponse = TempFreeBoardResponse.builder()
							.id(freeBoard.getId())
							.writer(freeBoard.getWriter())
							.title(freeBoard.getTitle())
							.contents(freeBoard.getContents())
							.icon(freeBoard.getIcon())
							.badge(freeBoard.getBadge())
							.createDate(freeBoard.getFormattedCreateDate())
							.count(freeBoard.getCount())
							.countOfAnswer(freeBoard.getCountOfAnswer())
							.build();
					return freeBoardResponse;
				})
				.collect(Collectors.toList());
		return freeBoardAnswerApiResponseList;
	}

	@Override
	public TempFreeBoardResponse createFreeBoard(UserEntity user, TempFreeBoardRequest request) {
		TempFreeBoard freeBoard = TempFreeBoard.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.icon(request.getIcon())
				.count(0)
				.build();
		TempFreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		
//		timelineService.createTimeline(createdFreeBoard);
		
		return response(createdFreeBoard);
	}

	@Override
	public TempFreeBoardResponse getOneFreeBoard(Long id) {
		TempFreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		return response(freeBoard);
	}

	@Override
	public TempFreeBoardResponse addFreeBoardCount(Long id) {
		TempFreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		freeBoard.addCount();
		return response(freeBoardRepository.save(freeBoard));
	}

	@Override
	public TempFreeBoardResponse updateFreeBoard(TempFreeBoardRequest request) {
		TempFreeBoard freeBoard = freeBoardRepository.findById(request.getId())
				.orElseThrow(NotFoundException::new);
		freeBoard.update(request);
		return response(freeBoardRepository.save(freeBoard));
	}

	@Override
	public void deleteFreeBoard(Long id) {
		TempFreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
//		timelineService.deleteTimeline(freeBoard);
//		List<FreeBoardAnswer> answerList = freeBoard.getFreeBoardAnswers();
//		for(int i = 0; i < answerList.size(); i++) {
//			FreeBoardAnswer freeBoardAnswer = answerList.get(i);
//			timelineService.deleteTimeline(freeBoardAnswer);
//			alertService.deleteAlert(freeBoardAnswer);
//		}
		freeBoardRepository.delete(freeBoard);
	}
	
	@Override
	public List<TempFreeBoard> findFreeBoardListByWriter(UserEntity loginUser) {
		return freeBoardRepository.findByWriter(loginUser);
	}
	
	private TempFreeBoardResponse response(TempFreeBoard freeBoard) {
		TempFreeBoardResponse freeBoardResponse = TempFreeBoardResponse.builder()
				.id(freeBoard.getId())
				.writer(freeBoard.getWriter())
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.icon(freeBoard.getIcon())
				.badge(freeBoard.getBadge())
				.createDate(freeBoard.getFormattedCreateDate())
				.count(freeBoard.getCount())
				.countOfAnswer(freeBoard.getCountOfAnswer())
				.build();
		List<TempFreeBoardAnswer> answerList = freeBoard.getTempFreeBoardAnswers();
		if(answerList != null) {
			List<FreeBoardAnswerApiResponse> freeBoardAnswerApiResponseList = answerList.stream()
					.map(answer -> {
						FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
								.id(answer.getId())
								.contents(answer.getContents())
								.countOfAnswers(answer.getTempFreeBoard().getCountOfAnswer())
								.freeBoardId(answer.getTempFreeBoard().getId())
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
