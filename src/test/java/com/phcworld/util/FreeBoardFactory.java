package com.phcworld.util;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
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
        User user = User.builder()
                .id(1L)
                .email("user@test.test")
                .password("user")
                .name("user")
                .build();
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(FreeBoard.class));

        Predicate<Field> writerIdPredicate = named("writer")
                .and(ofType(User.class))
                .and(inClass(FreeBoard.class));
        Predicate<Field> countPredicate = named("count")
                .and(ofType(Integer.class))
                .and(inClass(FreeBoard.class));

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

    public static FreeBoard getFreeBoardEntity(User user){
        return FreeBoard.builder()
                .writer(user)
                .title("test")
                .contents("test")
                .count(0)
                .badge("")
                .createDate(LocalDateTime.now())
                .build();
    }

    public static FreeBoardAnswer getFreeBoardAnswerEntity(User user, FreeBoard freeBoard){
        return FreeBoardAnswer.builder()
                .writer(user)
                .freeBoard(freeBoard)
                .contents("test")
                .build();
    }

}
