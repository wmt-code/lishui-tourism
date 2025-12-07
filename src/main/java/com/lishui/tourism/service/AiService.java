package com.lishui.tourism.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lishui.tourism.common.constant.Constants;
import com.lishui.tourism.common.context.UserContext;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.common.result.ResultCode;
import com.lishui.tourism.entity.*;
import com.lishui.tourism.mapper.*;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiService extends ServiceImpl<AiChatHistoryMapper, AiChatHistory> {

	private final ChatLanguageModel chatModel;
	private final ScenicSpotMapper scenicSpotMapper;
	private final ScenicTagRelMapper scenicTagRelMapper;
	private final TagMapper tagMapper;
	private final CommentMapper commentMapper;
	private final AiItineraryMapper aiItineraryMapper;

	/**
	 * 景点智能问答
	 */
	@Transactional(rollbackFor = Exception.class)
	public String spotChat(Long spotId, String question) {
		Long userId = UserContext.getUserId();

		// 获取景点详情
		ScenicSpot spot = scenicSpotMapper.selectById(spotId);
		if (spot == null) {
			throw new BusinessException(ResultCode.DATA_NOT_FOUND);
		}

		// 构建上下文：景点信息 + 标签 + 评论
		String context = buildSpotContext(spot);

		// 调用 AI
		String answer = callAiChat(context, question);

		// 保存聊天历史
		AiChatHistory history = new AiChatHistory();
		history.setUserId(userId);
		history.setSpotId(spotId);
		history.setQuestion(question);
		history.setAnswer(answer);
		history.setContext(context);
		baseMapper.insert(history);

		log.info("景点问答完成：spotId={}, userId={}", spotId, userId);
		return answer;
	}

	/**
	 * AI 行程生成
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> generateItinerary(Integer days, Double budget, String preference, String season) {
		Long userId = UserContext.getUserId();

		// 构建行程生成提示
		String prompt = buildItineraryPrompt(days, budget, preference, season);

		// 调用 AI 生成行程（返回 JSON 字符串）
		String itineraryJson = callAiChat(
				"你是一个专业的旅游行程规划师，根据用户需求生成结构化的JSON格式行程。",
				prompt
		);

		// 解析 JSON
		Map<String, Object> itineraryMap = JSONUtil.parseObj(itineraryJson);

		// 保存行程
		AiItinerary itinerary = new AiItinerary();
		itinerary.setUserId(userId);
		itinerary.setTitle(itineraryMap.getOrDefault("title", "丽水之旅").toString());
		itinerary.setDays(days);
		itinerary.setBudget(budget != null ? java.math.BigDecimal.valueOf(budget) : null);
		itinerary.setPreference(preference);
		itinerary.setSeason(season);
		itinerary.setItineraryData(itineraryJson);
		aiItineraryMapper.insert(itinerary);

		log.info("行程生成完成：userId={}, days={}", userId, days);
		return itineraryMap;
	}

	/**
	 * 获取用户聊天历史
	 */
	public List<AiChatHistory> getUserChatHistory(Long spotId, int limit) {
		Long userId = UserContext.getUserId();

		LambdaQueryWrapper<AiChatHistory> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AiChatHistory::getUserId, userId);

		if (spotId != null) {
			wrapper.eq(AiChatHistory::getSpotId, spotId);
		}

		wrapper.orderByDesc(AiChatHistory::getCreatedAt)
				.last("LIMIT " + limit);

		return baseMapper.selectList(wrapper);
	}

	/**
	 * 获取用户行程列表
	 */
	public List<AiItinerary> getUserItineraries() {
		Long userId = UserContext.getUserId();

		LambdaQueryWrapper<AiItinerary> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AiItinerary::getUserId, userId)
				.orderByDesc(AiItinerary::getCreatedAt);

		return aiItineraryMapper.selectList(wrapper);
	}

	/**
	 * 构建景点上下文
	 */
	private String buildSpotContext(ScenicSpot spot) {
		StringBuilder context = new StringBuilder();

		context.append("景点名称：").append(spot.getName()).append("\n");
		context.append("景点介绍：").append(spot.getDescription()).append("\n");
		context.append("地址：").append(spot.getAddress()).append("\n");
		context.append("开放时间：").append(spot.getOpeningHours()).append("\n");
		context.append("门票价格：").append(spot.getTicketPrice()).append("元\n");
		context.append("交通指南：").append(spot.getTrafficGuide()).append("\n");
		context.append("评分：").append(spot.getRating()).append("\n");

		// 添加标签
		List<Long> tagIds = scenicTagRelMapper.selectList(
				new LambdaQueryWrapper<com.lishui.tourism.entity.ScenicTagRel>()
						.eq(com.lishui.tourism.entity.ScenicTagRel::getSpotId, spot.getId())
		).stream().map(com.lishui.tourism.entity.ScenicTagRel::getTagId).collect(Collectors.toList());

		if (!tagIds.isEmpty()) {
			List<Tag> tags = tagMapper.selectBatchIds(tagIds);
			context.append("标签：");
			context.append(tags.stream().map(Tag::getName).collect(Collectors.joining("、")));
			context.append("\n");
		}

		// 添加部分评论
		List<Comment> comments = commentMapper.selectList(
				new LambdaQueryWrapper<Comment>()
						.eq(Comment::getSpotId, spot.getId())
						.eq(Comment::getStatus, Constants.Status.ONLINE)
						.orderByDesc(Comment::getRating)
						.last("LIMIT 3")
		);

		if (!comments.isEmpty()) {
			context.append("\n热门评论：\n");
			for (Comment comment : comments) {
				context.append("- ").append(comment.getContent())
						.append(" (评分：").append(comment.getRating()).append(")\n");
			}
		}

		return context.toString();
	}

	/**
	 * 构建行程生成提示
	 */
	private String buildItineraryPrompt(Integer days, Double budget, String preference, String season) {
		return String.format(
				"请为我生成一个丽水%d日游的行程规划，预算约%s元，偏好是%s，计划在%s出行。请以JSON格式返回行程数据。",
				days,
				budget != null ? budget : "不限",
				preference != null ? preference : "休闲游",
				season != null ? season : "任意季节"
		);
	}

	/**
	 * 调用 AI 对话
	 */
	private String callAiChat(String systemPrompt, String userPrompt) {
		try {
			dev.langchain4j.model.output.Response<AiMessage> response = chatModel.generate(
					List.of(
							SystemMessage.from(systemPrompt),
							UserMessage.from(userPrompt)
					)
			);
			return response.content().text();
		} catch (Exception e) {
			log.error("AI 调用失败：{}", e.getMessage());
			throw new BusinessException(ResultCode.AI_SERVICE_ERROR);
		}
	}
}