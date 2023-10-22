package com.phcworld.util;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

public class DiaryFactory {
    public static EasyRandom getDiaryRandomEntity(){
        User user = User.builder()
                .id(1L)
                .email("user@test.test")
                .password("user")
                .name("user")
                .build();
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Diary.class));

        Predicate<Field> writerIdPredicate = named("writer")
                .and(ofType(User.class))
                .and(inClass(Diary.class));

        Predicate<Field> countPredicate = named("countGood")
                .and(ofType(Long.class))
                .and(inClass(Diary.class));

        LocalDate firstDate = LocalDate.of(2010, 1, 1);
        LocalDate lastDate = LocalDate.now().minusDays(1);
        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(firstDate, lastDate)
                .randomize(writerIdPredicate, () -> user)
                .randomize(countPredicate, () -> (long) (Math.random() * 1000));

        return new EasyRandom(param);
    }

    public static Diary getDiaryEntity(User user){
        return Diary.builder()
                .writer(user)
                .title("test3")
                .contents("test3")
                .thumbnail("no-image-icon.gif")
                .createDate(LocalDateTime.now())
                .build();
    }

    public static DiaryAnswer getDiaryAnswerEntity(User user, Diary diary){
        return DiaryAnswer.builder()
                .writer(user)
                .diary(diary)
                .contents("test")
                .build();
    }

    public static EasyRandom getGoodEntity(){
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Good.class));

        Predicate<Field> userIdPredicate = named("user")
                .and(ofType(User.class))
                .and(inClass(Good.class));

        Predicate<Field> diaryIdPredicate = named("diary")
                .and(ofType(Diary.class))
                .and(inClass(Good.class));

        LocalDate firstDate = LocalDate.of(2012, 1, 1);
        LocalDate lastDate = LocalDate.now().minusDays(1);
        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(firstDate, lastDate)
                .randomize(userIdPredicate, () -> User.builder()
                        .id((long) (Math.random() * 300) + 1)
                        .build())
                .randomize(diaryIdPredicate, () -> Diary.builder()
                        .id((long) (Math.random() * 3000000) + 1)
                        .build());

        return new EasyRandom(param);
    }
}
