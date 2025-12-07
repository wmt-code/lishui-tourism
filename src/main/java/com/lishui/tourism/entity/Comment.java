package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long spotId;
    private Long userId;
    private String content;
    private Integer rating;
    private String images;
    private Long parentId;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
