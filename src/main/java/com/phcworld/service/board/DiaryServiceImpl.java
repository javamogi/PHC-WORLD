package com.phcworld.service.board;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.domain.board.DiaryHashtag;
import com.phcworld.domain.board.Hashtag;
import com.phcworld.domain.board.dto.DiaryResponseDto;
import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.CustomException;
import com.phcworld.repository.board.DiaryHashtagRepository;
import com.phcworld.repository.board.HashtagRepository;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.service.UserServiceImpl;
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

	private final UserServiceImpl userService;

	private final DiaryHashtagRepository diaryHashtagRepository;

	private final HashtagRepository hashtagRepository;

	@Transactional(readOnly = true)
	public DiaryResponseDto getDiaryResponseListTemp(UserEntity loginUser, String email, int pageNum, String searchKeyword) {
		UserEntity requestUser = userService.findUserByEmail(email);

		Page<DiarySelectDto> diaryPage = findPageDiary(loginUser, pageNum, requestUser, searchKeyword);

		return getDiaryResponseDto(diaryPage);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<DiarySelectDto> findPageDiary(UserEntity loginUser, Integer pageNum, UserEntity requestUser, String searchKeyword) {
//		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, Sort.by("good3").descending());
		if(isLoginUser(loginUser, requestUser)) {
			return diaryRepository.findAllPage(requestUser, pageRequest, searchKeyword);
		}
		return diaryRepository.findAllPage(loginUser, pageRequest, searchKeyword);
	}

	@Transactional(readOnly = true)
	public DiaryResponseDto findPageDiaryTemp2(UserEntity requestUser, Integer pageNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, Sort.by("id").descending());
		Page<DiarySelectDto> page = diaryRepository.findAllPage(requestUser, pageRequest, searchKeyword);
		return getDiaryResponseDto(page);
	}

	private boolean isLoginUser(UserEntity loginUser, UserEntity requestUser) {
		return loginUser == null || !requestUser.matchId(loginUser.getId());
	}

	@Transactional
	@Override
	public DiaryResponse createDiary(UserEntity user, DiaryRequest request) {
		Diary diary = Diary.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.thumbnail(request.getThumbnail())
				.countGood(0L)
				.build();
		Diary createdDiary = diaryRepository.save(diary);

		List<String> hashtags = request.getHashtags();
		int size = hashtags != null ? hashtags.size() : 0;
		for(int i = 0; i < size; i++){
			Hashtag hashtag = hashtagRepository.findByName(hashtags.get(i))
					.orElse(hashtagRepository.save(
							Hashtag.builder()
									.name(hashtags.get(i))
									.build()));
			diaryHashtagRepository.save(DiaryHashtag.builder()
							.diary(diary)
							.hashtag(hashtag)
							.build());
		}
		
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
	public List<Diary> findDiaryListByWriter(UserEntity loginUser) {
		return diaryRepository.findByWriter(loginUser);
	}

	@Transactional
	public SuccessResponse updateGood(Long diaryId, UserEntity loginUser) {
		Diary diary = diaryRepository.findById(diaryId)
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
		
		Diary updatedGoodCount = diaryRepository.save(goodService.pushGood(diary, loginUser));
		
		return SuccessResponse.builder()
				.success(Long.toString(updatedGoodCount.getCountGood()))
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
