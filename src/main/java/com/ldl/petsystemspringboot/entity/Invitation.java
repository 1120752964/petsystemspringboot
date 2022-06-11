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
public class Invitation {
    @TableId(value = "invitationid",type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long invitationid;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userid;
    private String title;
    private String pettype;
    private String type;
    private String context;
    private Integer liketnumber;
    private Integer collectnumber;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdtime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date changedtime;

}
