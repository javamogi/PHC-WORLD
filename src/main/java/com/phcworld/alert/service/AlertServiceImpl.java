package com.phcworld.alert.service;

import com.phcworld.alert.domain.dto.AlertResponseDto;
import com.phcworld.alert.infrasturcture.AlertEntity;
import com.phcworld.alert.infrasturcture.AlertJpaJpaRepository;
import com.phcworld.alert.service.port.AlertService;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.exception.model.CustomException;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
	
	private final AlertJpaJpaRepository alertJpaRepository;


	@Override
	public AlertEntity getOneAlert(Long id) {
		return alertJpaRepository.findById(id)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
	}
	
	@Override
	public List<AlertEntity> findListAlertByPostUser(UserEntity loginUser) {
//		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
		Page<AlertEntity> pageAlert = alertJpaRepository.findByPostWriter(loginUser, pageRequest);
		return pageAlert.getContent();
	}

	public List<AlertResponseDto> findByAlertListByPostUser(UserEntity loginUser){
		return alertJpaRepository.findAlertListByPostWriter(loginUser)
				.stream()
				.map(AlertResponseDto::of)
				.collect(Collectors.toList());
	}

	public AlertEntity createAlert(Good good) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.good(good)
				.postInfo(postInfo)
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
				.createDate(good.getCreateDate())
				.isRead(false)
				.build();
		return alertJpaRepository.save(alert);
	}
	
	public AlertEntity createAlert(DiaryAnswer diaryAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
//				.registerUser(diaryAnswer.getWriter())
//				.postWriter(diaryAnswer.getDiary().getWriter())
				.postInfo(postInfo)
				.createDate(diaryAnswer.getCreateDate())
				.isRead(false)
				.build();
		return alertJpaRepository.save(alert);
	}
	
	public AlertEntity createAlert(FreeBoardAnswerEntity freeBoardAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(freeBoardAnswer.getCreateDate())
				.isRead(false)
				.build();
		return alertJpaRepository.save(alert);
	}

	public AlertEntity createAlert(FreeBoardAnswer freeBoardAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.good(good)
				.postInfo(postInfo)
				.registerUser(UserEntity.from(freeBoardAnswer.getWriter()))
				.postWriter(UserEntity.from(freeBoardAnswer.getFreeBoard().getWriter()))
				.createDate(freeBoardAnswer.getCreateDate())
				.isRead(false)
				.build();
		return alertJpaRepository.save(alert);
	}
	
	public void deleteAlert(Good good) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		AlertEntity alert = alertJpaRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		alertJpaRepository.delete(alert);
	}
	
	public void deleteAlert(DiaryAnswer diaryAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity alert = alertJpaRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		alertJpaRepository.delete(alert);
	}
	
	public void deleteAlert(FreeBoardAnswerEntity freeBoardAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = alertJpaRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		alertJpaRepository.delete(alert);
	}

	public void deleteAlert(SaveType saveType, Long id){
		alertJpaRepository.deleteAlert(saveType, id);
	}

}
