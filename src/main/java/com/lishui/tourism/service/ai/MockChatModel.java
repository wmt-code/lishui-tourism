package com.lishui.tourism.service.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;

/**
 * Mock ChatModel - 无需真实 API Key 也能运行
 */
public class MockChatModel implements ChatLanguageModel {
    
    @Override
    public Response<dev.langchain4j.data.message.AiMessage> generate(java.util.List<dev.langchain4j.data.message.ChatMessage> messages) {
        String lastMessage = messages.isEmpty() ? "" : messages.get(messages.size() - 1).text();
        
        String mockResponse;
        
        // 判断是景点问答还是行程生成
        if (lastMessage.contains("行程") || lastMessage.contains("itinerary")) {
            mockResponse = generateMockItinerary();
        } else {
            mockResponse = generateMockSpotAnswer(lastMessage);
        }
        
        dev.langchain4j.data.message.AiMessage aiMessage = dev.langchain4j.data.message.AiMessage.from(mockResponse);
        return Response.from(aiMessage);
    }
    
    /**
     * 生成模拟的景点问答
     */
    private String generateMockSpotAnswer(String question) {
        if (question.contains("门票") || question.contains("价格")) {
            return "根据景点信息，该景点门票价格适中，性价比较高。建议您提前在网上预订可享受优惠。";
        } else if (question.contains("开放时间") || question.contains("营业")) {
            return "景点全年开放，建议您在早晨或傍晚时分前往，可以欣赏到最美的景色，同时避开人流高峰。";
        } else if (question.contains("交通") || question.contains("怎么去")) {
            return "您可以选择多种交通方式前往：1. 自驾车导航直达；2. 乘坐公交到达附近站点；3. 打车约需30分钟。建议使用地图软件查看实时路况。";
        } else if (question.contains("推荐") || question.contains("特色")) {
            return "这个景点融合了自然风光与人文历史，四季景色各异。春季赏花，夏季避暑，秋季观叶，冬季雪景都各有特色。建议您预留3-4小时游览时间。";
        } else {
            return "这是一个非常值得游览的景点。根据游客评价，景色优美、设施完善、服务周到。建议您提前做好攻略，带好相机记录美好时光。如需了解更多详细信息，欢迎继续提问！";
        }
    }
    
    /**
     * 生成模拟的行程数据
     */
    private String generateMockItinerary() {
        return """
                {
                  "title": "丽水精品3日游",
                  "days": 3,
                  "totalBudget": 1500,
                  "itinerary": [
                    {
                      "day": 1,
                      "title": "第一天：古堰画乡文化之旅",
                      "spots": [
                        {
                          "name": "古堰画乡",
                          "arriveTime": "09:00",
                          "duration": 180,
                          "description": "游览古堰画乡，感受江南水乡的宁静与艺术氛围",
                          "cost": 0
                        },
                        {
                          "name": "丽水市区",
                          "arriveTime": "14:00",
                          "duration": 120,
                          "description": "市区休闲购物，品尝当地美食",
                          "cost": 150
                        }
                      ],
                      "accommodation": "丽水市区酒店",
                      "accommodationCost": 200,
                      "dailyBudget": 350
                    },
                    {
                      "day": 2,
                      "title": "第二天：缙云仙都仙境探秘",
                      "spots": [
                        {
                          "name": "缙云仙都景区",
                          "arriveTime": "08:30",
                          "duration": 300,
                          "description": "游览5A级景区仙都，欣赏奇峰异石、山水画卷",
                          "cost": 60
                        }
                      ],
                      "accommodation": "缙云县城酒店",
                      "accommodationCost": 180,
                      "dailyBudget": 440
                    },
                    {
                      "day": 3,
                      "title": "第三天：返程与购物",
                      "spots": [
                        {
                          "name": "特产购物",
                          "arriveTime": "10:00",
                          "duration": 120,
                          "description": "购买丽水特产：香菇、木耳、蜂蜜等",
                          "cost": 200
                        }
                      ],
                      "accommodation": null,
                      "accommodationCost": 0,
                      "dailyBudget": 200
                    }
                  ],
                  "tips": [
                    "建议穿着舒适的运动鞋，方便徒步游览",
                    "随身携带防晒用品和雨具",
                    "提前预订景区门票可享优惠",
                    "尝试当地特色美食：缙云烧饼、丽水土鸡煲"
                  ]
                }
                """;
    }
}
