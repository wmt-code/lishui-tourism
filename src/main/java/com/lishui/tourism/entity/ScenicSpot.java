package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("scenic_spot")
public class ScenicSpot implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long destinationId;
    private String name;
    private String description;
    private String cover;
    private String address;
    private java.math.BigDecimal latitude;
    private java.math.BigDecimal longitude;
    private String openingHours;
    private java.math.BigDecimal ticketPrice;
    private String trafficGuide;
    private String level;
    private Integer hotScore;
    private java.math.BigDecimal rating;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
