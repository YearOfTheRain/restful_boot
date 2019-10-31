package com.restful;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestfulBootApplicationTests {

    @Test
    public void contextLoads() {
    }

    private void addValue(List<Integer> list) {
        list.add(3);
        list.add(10);
    }

    @Test
    public void testArrayList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        addValue(list);
        list.forEach(System.out::println);
    }

}
