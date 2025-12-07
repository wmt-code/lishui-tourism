package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("destination")
public class Destination implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String cover;
    private String province;
    private String city;
    private String district;
    private String address;
    private java.math.BigDecimal latitude;
    private java.math.BigDecimal longitude;
    private Integer hotScore;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
