package cn.mindit.atom.test.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("test_user")
public class TestUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private LocalDateTime deleteTime;

}
