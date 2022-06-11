package com.ldl.petsystemspringboot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
@Data
@Mapper
public class Test {
    @TableId(value = "likeorcollectid",type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long testid;
    private String value;
}
