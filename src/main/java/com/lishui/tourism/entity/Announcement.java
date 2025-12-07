package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("announcement")
public class Announcement implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private Long publisherId;
    private Integer status;
    private Integer viewCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
