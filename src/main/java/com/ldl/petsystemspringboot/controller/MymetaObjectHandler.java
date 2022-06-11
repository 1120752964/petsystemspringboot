package com.ldl.petsystemspringboot.controller;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MymetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createdtime",new Date(),metaObject);
        setFieldValByName("timetemp",new Date(),metaObject);
        setFieldValByName("changedtime",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("changedtime",new Date(),metaObject);
    }
}
