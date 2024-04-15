package com.phcworld.medium.util;

import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.user.infrastructure.UserEntity;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;
import static org.jeasy.random.FieldPredicates.inClass;

public class FreeBoardFactory {

    public static EasyRandom getFreeBoardRandomEntity(){
        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("user@test.test")
                .password("user")
                .name("user")
                .build();
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(FreeBoardEntity.class));

        Predicate<Field> writerIdPredicate = named("writer")
                .and(ofType(UserEntity.class))
                .and(inClass(FreeBoardEntity.class));
        Predicate<Field> countPredicate = named("count")
                .and(ofType(Integer.class))
                .and(inClass(FreeBoardEntity.class));

        LocalDate firstDate = LocalDate.of(2010, 1, 1);
        LocalDate lastDate = LocalDate.now().minusDays(1);
        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(firstDate, lastDate)
//                .randomize(Integer.class, new IntegerRandomizer(0, 1000))
                .randomize(writerIdPredicate, () -> user)
                .randomize(countPredicate, () -> (int) (Math.random() * 100000));

        return new EasyRandom(param);
    }

    public static FreeBoardEntity getFreeBoardEntity(UserEntity user){
        return FreeBoardEntity.builder()
                .writer(user)
                .title("test")
                .contents("test")
                .count(0)
                .createDate(LocalDateTime.now())
                .build();
    }

    public static FreeBoardAnswerEntity getFreeBoardAnswerEntity(UserEntity user, FreeBoardEntity freeBoard){
        return FreeBoardAnswerEntity.builder()
                .writer(user)
                .freeBoard(freeBoard)
                .contents("test")
                .build();
    }

}
