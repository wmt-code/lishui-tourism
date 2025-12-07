package com.lishui.tourism.controller;

import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.result.Result;
import com.lishui.tourism.config.security.RequireRole;
import com.lishui.tourism.entity.AiChatHistory;
import com.lishui.tourism.entity.AiItinerary;
import com.lishui.tourism.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AI 控制器
 */
@Tag(name = "AI 智能服务")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {
    
    private final AiService aiService;
    
    @Operation(summary = "景点智能问答")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping("/spot-chat")
    public Result<String> spotChat(@Valid @RequestBody SpotChatRequest request) {
        String answer = aiService.spotChat(request.getSpotId(), request.getQuestion());
        return Result.success(answer);
    }
    
    @Operation(summary = "AI 行程生成")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @PostMapping("/itinerary")
    public Result<Map<String, Object>> generateItinerary(@Valid @RequestBody ItineraryRequest request) {
        Map<String, Object> itinerary = aiService.generateItinerary(
                request.getDays(),
                request.getBudget(),
                request.getPreference(),
                request.getSeason()
        );
        return Result.success(itinerary);
    }
    
    @Operation(summary = "获取聊天历史")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @GetMapping("/chat-history")
    public Result<List<AiChatHistory>> getChatHistory(
            @RequestParam(required = false) Long spotId,
            @RequestParam(defaultValue = "10") int limit) {
        List<AiChatHistory> history = aiService.getUserChatHistory(spotId, limit);
        return Result.success(history);
    }
    
    @Operation(summary = "获取我的行程列表")
    @RequireRole({Constants.Role.TOURIST, Constants.Role.GUIDE, Constants.Role.ADMIN})
    @GetMapping("/my-itineraries")
    public Result<List<AiItinerary>> getMyItineraries() {
        List<AiItinerary> itineraries = aiService.getUserItineraries();
        return Result.success(itineraries);
    }
    
    @Data
    public static class SpotChatRequest {
        @NotNull(message = "景点ID不能为空")
        private Long spotId;
        
        @NotBlank(message = "问题不能为空")
        private String question;
    }
    
    @Data
    public static class ItineraryRequest {
        @NotNull(message = "天数不能为空")
        private Integer days;
        
        private Double budget;
        
        private String preference;
        
        private String season;
    }
}
