package com.phcworld.repository.board;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryHashtag;
import com.phcworld.domain.board.Hashtag;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class DiaryHashtagRepositoryTest {

    @Autowired
    private DiaryHashtagRepository diaryHashtagRepository;

    @Test
    @Ignore
    public void insert(){
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(DiaryHashtag.class));

        Predicate<Field> hashtagPredicate = named("hashtag")
                .and(ofType(Hashtag.class))
                .and(inClass(DiaryHashtag.class));

        Predicate<Field> diaryIdPredicate = named("diary")
                .and(ofType(Diary.class))
                .and(inClass(DiaryHashtag.class));

        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .randomize(hashtagPredicate, () -> Hashtag.builder()
                        .id((long) (Math.random() * 14) + 1)
                        .build())
                .randomize(diaryIdPredicate, () -> Diary.builder()
                        .id((long) (Math.random() * 3000000) + 1)
                        .build());

        EasyRandom easyRandom = new EasyRandom(param);
        for(int i = 0; i < 10000; i++){
            DiaryHashtag diaryHashtag = easyRandom.nextObject(DiaryHashtag.class);
            diaryHashtagRepository.save(diaryHashtag);
        }

    }

}