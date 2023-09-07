package com.phcworld.service.board;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.domain.board.FreeBoardSearchDto;
import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.repository.board.dto.FreeBoardSelectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
//@Transactional
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {
	private final FreeBoardRepository freeBoardRepository;
	
	private final TimelineServiceImpl timelineService;
	
	private final AlertServiceImpl alertService;

	@Transactional(readOnly = true)
	@Override
	public List<FreeBoardResponse> findFreeBoardAllListAndSetNewBadge() {
		List<FreeBoard> list = freeBoardRepository.findAll();
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

	@Transactional
	@Override
	public FreeBoardResponse getOneFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		return response(freeBoard);
	}

	@Transactional
	@Override
	public FreeBoardResponse addFreeBoardCount(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		freeBoard.addCount();
		return response(freeBoardRepository.save(freeBoard));
	}

	@Transactional
	@Override
	public FreeBoardResponse updateFreeBoard(FreeBoardRequest request) {
		FreeBoard freeBoard = freeBoardRepository.findById(request.getId())
				.orElseThrow(NotFoundException::new);
		freeBoard.update(request);
		return response(freeBoardRepository.save(freeBoard));
	}

	@Transactional
	@Override
	public void deleteFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		timelineService.deleteTimeline(SaveType.FREE_BOARD, id);
		freeBoardRepository.delete(freeBoard);
	}

	@Transactional
	@Override
	public List<FreeBoard> findFreeBoardListByWriter(User loginUser) {
		return freeBoardRepository.findByWriter(loginUser);
	}

	private FreeBoardResponse response(FreeBoard freeBoard) {
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
