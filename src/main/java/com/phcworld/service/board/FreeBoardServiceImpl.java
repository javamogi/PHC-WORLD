package com.phcworld.service.board;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.exception.model.CustomException;
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
		List<FreeBoardResponse> freeBoardAnswerApiResponseList = list.stream()
				.map(FreeBoardResponse::of)
				.collect(Collectors.toList());
		return freeBoardAnswerApiResponseList;
	}

	@Transactional(readOnly = true)
	@Override
	public List<FreeBoardResponse> getSearchResult(int pageNum, int pageSize, String keyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.by("createDate").descending());
		List<FreeBoardSelectDto> list = freeBoardRepository.findByKeywordOrderById(keyword, pageRequest);
		List<FreeBoardResponse> freeBoardAnswerApiResponseList = list.stream()
				.map(FreeBoardResponse::of)
				.collect(Collectors.toList());
		return freeBoardAnswerApiResponseList;
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
				.orElseThrow(() -> new CustomException("400", "게시글이 없습니다."));
		return response(freeBoard);
	}

	@Transactional
	@Override
	public FreeBoardResponse addFreeBoardCount(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(() -> new CustomException("400", "게시글이 없습니다."));
		freeBoard.addCount();
		return response(freeBoardRepository.save(freeBoard));
	}

	@Transactional
	@Override
	public FreeBoardResponse updateFreeBoard(FreeBoardRequest request) {
		FreeBoard freeBoard = freeBoardRepository.findById(request.getId())
				.orElseThrow(() -> new CustomException("400", "게시글이 없습니다."));
		freeBoard.update(request);
		return response(freeBoardRepository.save(freeBoard));
	}

	@Transactional
	@Override
	public void deleteFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(() -> new CustomException("400", "게시글이 없습니다."));
		timelineService.deleteTimeline(freeBoard);
		List<FreeBoardAnswer> answerList = freeBoard.getFreeBoardAnswers();
		for(int i = 0; i < answerList.size(); i++) {
			FreeBoardAnswer freeBoardAnswer = answerList.get(i);
			timelineService.deleteTimeline(freeBoardAnswer);
			alertService.deleteAlert(freeBoardAnswer);
		}
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
