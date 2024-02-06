package com.phcworld.repository.board;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryHashtag;
import com.phcworld.domain.board.DiaryInsertDto;
import com.phcworld.domain.board.Hashtag;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.util.DiaryFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jeasy.random.FieldPredicates.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class DiaryHashtagRepositoryTest {

    @Autowired
    private DiaryHashtagRepository diaryHashtagRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DiaryRepository diaryRepository;

    @Test
    public void insertHashtag(){

        List<Hashtag> list = IntStream.range(0, 10000 * 10)
                .parallel()
                .mapToObj(i -> Hashtag.builder()
                        .name(UUID.randomUUID().toString())
                        .build())
                .collect(Collectors.toList());

        String sql = String.format("INSERT INTO %s (name) VALUES (:name)", "hashtag");

        SqlParameterSource[] params = list
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);

//        for(int i = 0; i < 100; i++) {
//            Hashtag hashtag = Hashtag.builder()
//                    .name("#" + i)
//                    .build();
//            hashtagRepository.save(hashtag);
//        }
    }

    @Test
//    @Ignore
    public void insert(){

        @Getter
        @AllArgsConstructor
        class DiaryHashtagInsertDto {
            private Long diaryId;
            private Long hashtagId;
        }

        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(DiaryHashtag.class));

        Predicate<Field> hashtagPredicate = named("hashtag")
                .and(ofType(Hashtag.class))
                .and(inClass(DiaryHashtag.class));

        Predicate<Field> diaryIdPredicate = named("diary")
                .and(ofType(Diary.class))
                .and(inClass(DiaryHashtag.class));

        long[] arr = {

        };

        long[] arr2 = {

        };

        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .randomize(hashtagPredicate, () -> Hashtag.builder()
//                        .id((long) (Math.random() * (10000 * 10)) + 1)
                        .id(888L)
//                        .id(arr2[(int) (Math.random() * arr.length)])
                        .build())
                .randomize(diaryIdPredicate, () -> Diary.builder()
                        .id((long) (Math.random() * (10000 * 300)) + 1)
//                        .id(arr[(int) (Math.random() * arr.length)])
                        .build());

        EasyRandom easyRandom = new EasyRandom(param);
//        for(int i = 0; i < 10000; i++){
//            DiaryHashtag diaryHashtag = easyRandom.nextObject(DiaryHashtag.class);
//            DiaryHashtag diaryHashtag = easyRandom.nextObject(DiaryHashtag.class);
//            diaryHashtagRepository.save(diaryHashtag);
//        }

        List<DiaryHashtagInsertDto> list = IntStream.range(0, 10000 * 100)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(DiaryHashtag.class))
                .map(dh -> {
                    return new DiaryHashtagInsertDto(dh.getDiary().getId(), dh.getHashtag().getId());
                })
                .collect(Collectors.toList());

        String sql = String.format("INSERT INTO %s (diary_id, hashtag_id) VALUES (:diaryId, :hashtagId)", "diary_hashtag");

        SqlParameterSource[] params = list
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);

    }

}