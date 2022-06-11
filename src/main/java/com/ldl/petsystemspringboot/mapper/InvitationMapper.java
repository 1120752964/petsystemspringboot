package com.ldl.petsystemspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldl.petsystemspringboot.entity.Image;
import com.ldl.petsystemspringboot.entity.Invitation;

public interface InvitationMapper extends BaseMapper<Invitation> {
    int insert(Invitation user);
}
