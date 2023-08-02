package com.phcworld.util;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.number.IntegerRandomizer;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;
import static org.jeasy.random.FieldPredicates.inClass;

public class FreeBoardFactory {

    public static EasyRandom getFreeBoardEntity(){
        User user = User.builder()
                .id(1L)
                .email("user@test.test")
                .password("user")
                .name("user")
                .build();
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(FreeBoard.class));

        Predicate<Field> memberIdPredicate = named("writer")
                .and(ofType(User.class))
                .and(inClass(FreeBoard.class));
        LocalDate firstDate = LocalDate.of(2010, 1, 1);
        LocalDate lastDate = LocalDate.now();
        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(firstDate, lastDate)
//                .randomize(Integer.class, new IntegerRandomizer(0, 1000))
                .randomize(memberIdPredicate, () -> user);

        return new EasyRandom(param);
    }
}
