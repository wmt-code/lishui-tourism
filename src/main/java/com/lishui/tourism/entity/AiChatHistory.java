package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("ai_chat_history")
public class AiChatHistory implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long spotId;
    private String question;
    private String answer;
    private String context;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableLogic
    private Integer deleted;
}
