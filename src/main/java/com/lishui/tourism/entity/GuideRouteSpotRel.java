package com.lishui.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("guide_route_spot_rel")
public class GuideRouteSpotRel implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long routeId;
    private Long spotId;
    private Integer dayNumber;
    private Integer sortOrder;
    private Integer duration;
    private String description;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
