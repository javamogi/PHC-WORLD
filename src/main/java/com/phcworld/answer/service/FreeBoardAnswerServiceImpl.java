package com.phcworld.answer.service;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.answer.infrastructure.FreeBoardAnswerRepository;
import com.phcworld.answer.service.port.FreeBoardAnswerService;
import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.exception.model.AnswerNotFoundException;
import com.phcworld.exception.model.FreeBoardNotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class FreeBoardAnswerServiceImpl implements FreeBoardAnswerService {
	
	private final FreeBoardRepository freeBoardRepository;
	private final FreeBoardAnswerRepository freeBoardAnswerRepository;
	private final LocalDateTimeHolder localDateTimeHolder;

//	private final AlertServiceImpl alertService;
//
//	private final TimelineServiceImpl timelineService;
	
	@Transactional
	@Override
	public FreeBoardAnswer register(long freeBoardId, User user, FreeBoardAnswerRequest request) {
		FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
				.orElseThrow(FreeBoardNotFoundException::new);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.from(freeBoard, user, request, localDateTimeHolder);
		FreeBoardAnswer savedFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);

//		timelineService.createTimeline(savedFreeBoardAnswer);
//
//		if(!freeBoard.matchWriter(UserEntity.from(user))) {
//			alertService.createAlert(savedFreeBoardAnswer);
//		}

		return savedFreeBoardAnswer;
	}

	@Override
	public FreeBoardAnswer getById(long id, UserEntity loginUser) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(id)
				.orElseThrow(AnswerNotFoundException::new);
		if(!answer.matchWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return answer;
	}

	@Override
	public FreeBoardAnswer update(FreeBoardAnswerUpdateRequest request, UserEntity loginUser) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(request.getId())
				.orElseThrow(AnswerNotFoundException::new);
		if(!answer.matchWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		answer = answer.update(request, localDateTimeHolder);
		return freeBoardAnswerRepository.save(answer);
	}

	@Override
	public SuccessResponse delete(long id, UserEntity loginUser) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(id)
						.orElseThrow(AnswerNotFoundException::new);
		freeBoardAnswerRepository.deleteById(answer.getId());
		return SuccessResponse.builder()
				.success("삭제 성공")
				.build();
	}

	@Override
	public Page<FreeBoardAnswer> getListByFreeBoard(long freeBoardId, int pageNum){
		Pageable pageable = PageRequest.of(pageNum - 1, 10);
//		Page<FreeBoardAnswer> answerPage = freeBoardAnswerRepository.findByFreeBoardId(freeBoardId, pageable);
//		return answerPage.getContent();
		return freeBoardAnswerRepository.findByFreeBoardId(freeBoardId, pageable);
	}
}
