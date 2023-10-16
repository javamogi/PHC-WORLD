package com.phcworld.service.board;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.domain.board.dto.DiaryResponseDto;
import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.CustomException;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.good.GoodService;
import com.phcworld.service.timeline.TimelineServiceImpl;


@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
	
	private final DiaryRepository diaryRepository;
	
	private final AlertServiceImpl alertService;
	
	private final TimelineServiceImpl timelineService;
	
	private final GoodService goodService;

	private final UserService userService;

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public DiaryResponseDto getDiaryResponseListTemp(User loginUser, String email, int pageNum) {
		User requestUser = userService.findUserByEmail(email);

		Page<DiarySelectDto> diaryPage = findPageDiary(loginUser, pageNum, requestUser);

		return getDiaryResponseDto(diaryPage);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<DiarySelectDto> findPageDiary(User loginUser, Integer pageNum, User requestUser) {
//		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, Sort.by("createDate").descending());
		if(isLoginUser(loginUser, requestUser)) {
			return diaryRepository.findAllPage(requestUser, pageRequest);
		}
		return diaryRepository.findAllPage(loginUser, pageRequest);
	}

	@Transactional(readOnly = true)
	public DiaryResponseDto findPageDiaryTemp2(User requestUser, Integer pageNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, Sort.by("id").descending());
		Page<DiarySelectDto> page = diaryRepository.findAllPage(requestUser, pageRequest);
		return getDiaryResponseDto(page);
	}

	private boolean isLoginUser(User loginUser, User requestUser) {
		return loginUser == null || !requestUser.matchId(loginUser.getId());
	}

	@Transactional
	@Override
	public DiaryResponse createDiary(User user, DiaryRequest request) {
		Diary diary = Diary.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.thumbnail(request.getThumbnail())
				.countGood(0L)
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		
		timelineService.createTimeline(createdDiary);
		
		return response(diary);
	}

	@Transactional
	@Override
	public DiaryResponse getOneDiary(Long id) {
		Diary diary = diaryRepository.findById(id)
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
		return response(diary);
	}

	@Transactional
	@Override
	public DiaryResponse updateDiary(DiaryRequest request) {
		Diary diary = diaryRepository.findById(request.getId())
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
		diary.update(request);
		return response(diaryRepository.save(diary));
	}

	@Transactional
	@Override
	public void deleteDiary(Long id) {
		Diary diary = diaryRepository.findById(id)
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
		timelineService.deleteTimeline(SaveType.DIARY, diary.getId());
		alertService.deleteAlert(SaveType.DIARY, diary.getId());
		diaryRepository.delete(diary);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Diary> findDiaryListByWriter(User loginUser) {
		return diaryRepository.findByWriter(loginUser);
	}

	@Transactional
	public SuccessResponse updateGood(Long diaryId, User loginUser) {
		Diary diary = diaryRepository.findById(diaryId)
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
		
		Diary updatedGoodCount = diaryRepository.save(goodService.pushGood(diary, loginUser));
		
		return SuccessResponse.builder()
				.success(Integer.toString(updatedGoodCount.getCountOfGood()))
				.build();
	}
	
	private DiaryResponse response(Diary diary) {
		DiaryResponse diaryResponse = DiaryResponse.of(diary);
		List<DiaryAnswer> answerList = diary.getDiaryAnswers();
		if(answerList != null) {
			List<DiaryAnswerApiResponse> diaryAnswerApiResponseList = answerList.stream()
					.map(DiaryAnswerApiResponse::of)
					.collect(Collectors.toList());
			diaryResponse.setDiaryAnswerList(diaryAnswerApiResponseList);
		}
		return diaryResponse;
	}

	private DiaryResponseDto getDiaryResponseDto(Page<DiarySelectDto> diaryPage) {
		List<DiaryResponse> diaryResponseList = diaryPage.getContent().stream()
				.map(DiaryResponse::of)
				.collect(Collectors.toList());
		return DiaryResponseDto.builder()
				.totalPages(diaryPage.getTotalPages())
				.diaries(diaryResponseList)
				.build();
	}
	
}
