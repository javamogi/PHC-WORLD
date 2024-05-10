package com.phcworld.freeboard.infrastructure;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class FreeBoardJpaRepositoryTest {

    @Autowired
    private FreeBoardJpaRepository freeBoardRepository;

    @Test
    public void test(){
        List<FreeBoardEntity> list = freeBoardRepository.findAllByFetchTemp();
        for (FreeBoardEntity f : list) {
            log.info("f : {}", f);
        }
    }

}