package com.ldl.petsystemspringboot;

import com.ldl.petsystemspringboot.entity.User;
import com.ldl.petsystemspringboot.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@MapperScan("com.ldl.petsystemspringboot.mapper")
@SpringBootTest
class PetsystemspringbootApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void login(){
        List<User> userList = userMapper.selectList(null);
        for (int i = 0; i < userList.size(); i++) {
            System.out.println(userList.get(i));

        }
    }

}
