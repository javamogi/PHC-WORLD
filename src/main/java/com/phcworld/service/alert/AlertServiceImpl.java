package com.phcworld.service.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.dto.AlertResponseDto;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import com.phcworld.repository.alert.AlertRepository;
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
	
	private final AlertRepository alertRepository;
	
	@Override
	public Alert getOneAlert(Long id) {
		return alertRepository.findById(id)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
	}
	
	@Override
	public List<Alert> findListAlertByPostUser(User loginUser) {
//		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
		Page<Alert> pageAlert = alertRepository.findByPostWriter(loginUser, pageRequest);
		return pageAlert.getContent();
	}

	public List<AlertResponseDto> findByAlertListByPostUser(User loginUser){
		return alertRepository.findAlertListByPostWriter(loginUser)
				.stream()
				.map(AlertResponseDto::of)
				.collect(Collectors.toList());
	}

	public Alert createAlert(Good good) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Alert alert = Alert.builder()
//				.type("Diary")
//				.good(good)
				.postInfo(postInfo)
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
				.createDate(good.getCreateDate())
				.read(false)
				.build();
		return alertRepository.save(alert);
	}
	
	public Alert createAlert(DiaryAnswer diaryAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		Alert alert = Alert.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
//				.registerUser(diaryAnswer.getWriter())
//				.postWriter(diaryAnswer.getDiary().getWriter())
				.postInfo(postInfo)
				.createDate(diaryAnswer.getCreateDate())
				.read(false)
				.build();
		return alertRepository.save(alert);
	}
	
	public Alert createAlert(FreeBoardAnswer freeBoardAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Alert alert = Alert.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(freeBoardAnswer.getCreateDate())
				.read(false)
				.build();
		return alertRepository.save(alert);
	}
	
	public void deleteAlert(Good good) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Alert alert = alertRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		alertRepository.delete(alert);
	}
	
	public void deleteAlert(DiaryAnswer diaryAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		Alert alert = alertRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		alertRepository.delete(alert);
	}
	
	public void deleteAlert(FreeBoardAnswer freeBoardAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Alert alert = alertRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		alertRepository.delete(alert);
	}

	public void deleteDiaryAlert(SaveType saveType, Long id){
		alertRepository.deleteAlert(saveType, id);
	}
	
}
