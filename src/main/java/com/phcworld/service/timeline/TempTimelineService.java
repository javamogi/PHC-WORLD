package com.phcworld.service.timeline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.domain.user.User;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.service.good.GoodService;

@Service
public class TempTimelineService {
	
	@Autowired
	private FreeBoardServiceImpl freeBoardService;

	@Autowired
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@Autowired
	private DiaryServiceImpl diaryService;
	
	@Autowired
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@Autowired
	private GoodService goodService;
	
	public List<TempTimeline> getTimeline(User loginUser) {
		List<TempTimeline> timelineList = new ArrayList<TempTimeline>();
		
		List<FreeBoard> freeBoardList = freeBoardService.findFreeBoardListByWriter(loginUser);
		for(int i = 0; i < freeBoardList.size(); i++) {
			TempTimeline temp = TempTimeline.builder()
					.type("free board")
					.url("/freeboards/"+freeBoardList.get(i).getId()+"/detail")
					.icon("list-alt")
					.freeBoard(freeBoardList.get(i))
					.saveDate(freeBoardList.get(i).getCreateDate())
					.build();
			timelineList.add(temp);
			if(i == 5) {
				break;
			}
		}
		
		List<FreeBoardAnswer> freeBoardAnswerList = freeBoardAnswerService.findFreeBoardAnswerListByWriter(loginUser);
		for(int i = 0; i < freeBoardAnswerList.size(); i++) {
			TempTimeline temp = TempTimeline.builder()
					.type("free board answer")
					.icon("comment")
					.url("/freeboards/"+freeBoardAnswerList.get(i).getFreeBoard().getId()+"/detail")
					.freeBoardAnswer(freeBoardAnswerList.get(i))
					.saveDate(freeBoardAnswerList.get(i).getCreateDate())
					.build();
			timelineList.add(temp);
			if(i == 5) {
				break;
			}
		}
		
		List<Diary> diaryList = diaryService.findDiaryListByWriter(loginUser);
		for(int i = 0; i < diaryList.size(); i++) {
			TempTimeline temp = TempTimeline.builder()
					.type("diary")
					.icon("edit")
					.url("/diary/"+diaryList.get(i).getId()+"/detail")
					.diary(diaryList.get(i))
					.saveDate(diaryList.get(i).getCreateDate())
					.build();
			timelineList.add(temp);
			if(i == 5) {
				break;
			}
		}
		
		List<DiaryAnswer> diaryAnswerList = diaryAnswerService.findDiaryAnswerListByWriter(loginUser);
		for(int i = 0; i < diaryAnswerList.size(); i++) {
			TempTimeline temp = TempTimeline.builder()
					.type("diary answer")
					.icon("comment")
					.url("/diary/"+diaryAnswerList.get(i).getDiary().getId()+"/detail")
					.diaryAnswer(diaryAnswerList.get(i))
					.saveDate(diaryAnswerList.get(i).getCreateDate())
					.build();
			timelineList.add(temp);
			if(i == 5) {
				break;
			}
		}
		
		List<Good> goodList = goodService.getGoodList(loginUser);
		for(int i = 0; i < goodList.size(); i++) {
			TempTimeline temp = TempTimeline.builder()
					.type("good")
					.icon("thumbs-up")
					.url("/diary/"+goodList.get(i).getDiary().getId()+"/detail")
					.good(goodList.get(i))
					.saveDate(goodList.get(i).getCreateDate())
					.build();
			timelineList.add(temp);
			if(i == 5) {
				break;
			}
		}
		Comparator<TempTimeline> compTimeline = new Comparator<TempTimeline>() {
			@Override
			public int compare(TempTimeline a, TempTimeline b) {
				return b.getSaveDate().compareTo(a.getSaveDate());
			}
		};
		
		timelineList.sort(compTimeline);
		return timelineList;
	}
	
}
