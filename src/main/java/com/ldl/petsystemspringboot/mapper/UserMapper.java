package com.ldl.petsystemspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldl.petsystemspringboot.entity.User;

import java.io.Serializable;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    int insert(User user);
}
