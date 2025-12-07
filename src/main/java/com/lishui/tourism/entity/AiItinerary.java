package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("ai_itinerary")
public class AiItinerary implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private Integer days;
    private java.math.BigDecimal budget;
    private String preference;
    private String season;
    private String itineraryData;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
