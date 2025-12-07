package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("scenic_tag_rel")
public class ScenicTagRel implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long spotId;
    private Long tagId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
