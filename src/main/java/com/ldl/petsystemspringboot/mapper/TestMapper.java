package com.ldl.petsystemspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldl.petsystemspringboot.entity.Likeorcollect;

public interface TestMapper extends BaseMapper<TestMapper> {

    int insert(Likeorcollect user);

}
