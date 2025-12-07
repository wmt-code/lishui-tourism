package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("favorite")
public class Favorite implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String targetType;
    private Long targetId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
