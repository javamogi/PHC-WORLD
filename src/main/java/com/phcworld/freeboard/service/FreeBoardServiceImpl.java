package com.phcworld.freeboard.service;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.freeboard.controller.port.FreeBoardSearchDto;
import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.infrastructure.FreeBoardJpaRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
//@Transactional
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {
	private final FreeBoardJpaRepository freeBoardRepository;
	
	private final TimelineServiceImpl timelineService;
	
	private final AlertServiceImpl alertService;

	@Transactional(readOnly = true)
	@Override
	public List<FreeBoardResponse> findFreeBoardAllListAndSetNewBadge() {
		List<FreeBoardEntity> list = freeBoardRepository.findAll();
		return list.stream()
				.map(FreeBoardResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<FreeBoardResponse> getSearchResult(FreeBoardSearchDto search) {
		PageRequest pageRequest = PageRequest.of(search.getPageNum() - 1, search.getPageSize(), Sort.by("createDate").descending());
		List<FreeBoardSelectDto> list = freeBoardRepository.findByKeywordOrderById(search.getKeyword(), pageRequest);
		return list.stream()
				.map(FreeBoardResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public FreeBoardResponse createFreeBoard(UserEntity user, FreeBoardRequest request) {
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.icon(request.getIcon())
				.count(0)
				.build();
		FreeBoardEntity createdFreeBoard = freeBoardRepository.save(freeBoard);
		
		timelineService.createTimeline(createdFreeBoard);
		
		return response(createdFreeBoard);
	}

	@Transactional
	@Override
	public FreeBoardResponse getOneFreeBoard(Long id) {
		FreeBoardEntity freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		return response(freeBoard);
	}

	@Transactional
	@Override
	public FreeBoardResponse addFreeBoardCount(Long id) {
		FreeBoardEntity freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		freeBoard.addCount();
		return response(freeBoard);
	}

	@Transactional
	@Override
	public FreeBoardResponse updateFreeBoard(FreeBoardRequest request) {
		FreeBoardEntity freeBoard = freeBoardRepository.findById(request.getId())
				.orElseThrow(NotFoundException::new);
		freeBoard.update(request);
		return response(freeBoard);
	}

	@Transactional
	@Override
	public void deleteFreeBoard(Long id) {
		FreeBoardEntity freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		timelineService.deleteTimeline(SaveType.FREE_BOARD, id);
		alertService.deleteAlert(SaveType.FREE_BOARD, id);
		freeBoardRepository.delete(freeBoard);
	}

	@Transactional
	@Override
	public List<FreeBoardEntity> findFreeBoardListByWriter(UserEntity loginUser) {
		return freeBoardRepository.findByWriter(loginUser);
	}

	private FreeBoardResponse response(FreeBoardEntity freeBoard) {
		FreeBoardResponse freeBoardResponse = FreeBoardResponse.of(freeBoard);
		List<FreeBoardAnswer> answerList = freeBoard.getFreeBoardAnswers();
		if(answerList != null) {
			List<FreeBoardAnswerApiResponse> freeBoardAnswerApiResponseList = answerList.stream()
					.map(FreeBoardAnswerApiResponse::of)
					.collect(Collectors.toList());
			freeBoardResponse.setFreeBoardAnswerList(freeBoardAnswerApiResponseList);
		}
		return freeBoardResponse;
	}
	
}
