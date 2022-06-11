package com.ldl.petsystemspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldl.petsystemspringboot.entity.Comment;
import com.ldl.petsystemspringboot.entity.Likeorcollect;
import com.ldl.petsystemspringboot.entity.User;

import java.util.List;

public interface LikeorcollectMapper extends BaseMapper<Likeorcollect> {

    int insert(Likeorcollect user);

}
