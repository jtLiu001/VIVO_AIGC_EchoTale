package com.VIVO.echotale.ApiTest;

import com.VIVO.echotale.utils.*; // 导入自定义工具类
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; // 用于 JSON 处理
import org.springframework.http.*; // 导入 HTTP 相关的类
import org.springframework.stereotype.Service; // 导入服务注解
import org.springframework.web.client.RestTemplate; // 导入 RestTemplate 用于发送 HTTP 请求

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap; // 导入 HashMap
import java.util.List;
import java.util.Map; // 导入 Map
import java.util.UUID; // 导入 UUID 用于生成唯一标识符

@Service // 标注这是一个服务类，Spring 会自动检测并注册
public class VivoService {

    public String vivogpt() throws Exception { // 方法签名，抛出异常

        String appId = "3032080331"; // 替换为你的 appId
        String appKey = "rHHmLmBRaqsMMXnK"; // 替换为你的 appKey
        String URI = "/vivogpt/completions"; // 请求的 URI
        String DOMAIN = "api-ai.vivo.com.cn"; // 请求的域名
        String METHOD = "POST"; // HTTP 方法
        UUID requestId = UUID.randomUUID(); // 生成唯一的请求 ID
        System.out.println("requestId: " + requestId); // 打印请求 ID

        // 构建查询字符串
        Map<String, Object> map = new HashMap<>();
        map.put("requestId", requestId.toString()); // 将 requestId 添加到查询参数中
        String queryStr = mapToQueryString(map); // 将查询参数转换为查询字符串


        // 构建messages，包含多轮对话
        List<Map<String, String>> messages = loadHistory();
        
        // 添加历史对话
        // messages.add(createMessage("user", "你是谁？"));
        // messages.add(createMessage("assistant", "你好，我是蓝心小V，你的虚拟伙伴和闲聊好友。无论你心情如何，希望与你分享的话题有多么轻松或深奥，我都在这里随时准备和你聊上几句。所以，告诉我，今天的你，想要开始我们的对话从哪里呢？"));


        // 添加最新的用户输入
        messages.add(createMessage("user", "你是不是傻"));


        // 构建请求体
        Map<String, Object> data = new HashMap<>();

        // data.put("prompt", "请告诉我你会什么？"); // 替换为你实际的 prompt

        data.put("messages", messages);
        data.put("model", "vivo-BlueLM-TB"); // 模型名称
        UUID sessionId = UUID.randomUUID(); // 生成唯一的会话 ID
        data.put("sessionId", sessionId.toString()); // 将 sessionId 添加到请求体中
        System.out.println(sessionId); // 打印会话 ID

        // 生成认证头
        HttpHeaders headers = VivoAuth.generateAuthHeaders(appId, appKey, METHOD, URI, queryStr); // 生成认证头
        headers.add("Content-Type", "application/json"); // 添加内容类型头
        System.out.println(headers); // 打印请求头

        // 构建请求 URL
        String url = String.format("http://%s%s?%s", DOMAIN, URI, queryStr); // 拼接 URL
        String requestBodyString = new ObjectMapper().writeValueAsString(data); // 将请求体转换为 JSON 字符串
        RestTemplate restTemplate = new RestTemplate(); // 创建 RestTemplate 实例
        HttpHeaders httpHeaders = new HttpHeaders(); // 创建新的 HttpHeaders 实例
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)); // 设置内容类型为 JSON
        httpHeaders.addAll(headers); // 将生成的认证头添加到 HttpHeaders 实例中
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyString, httpHeaders); // 构建 HttpEntity 实例，包含请求体和请求头

        // 发送 HTTP 请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class); // 发送 POST 请求并获取响应
        if (response.getStatusCode() == HttpStatus.OK) { // 检查响应状态码
            System.out.println("Response body: " + response.getBody()); // 打印响应体

            // 从响应中提取 content
            String assistantResponse = extractContentFromResponse(response.getBody());
            // 将 assistant 的回复添加到消息记录中
            messages.add(createMessage("assistant", assistantResponse));

            saveHistory(messages);//存储历史记录

        } else {
            System.out.println("Error response: " + response.getStatusCode()); // 打印错误状态码
        }
        return response.getBody(); // 返回响应体
    }



    //overload
    public String vivogpt(String question) throws Exception { // 方法签名，抛出异常

        String appId = "3032080331"; // 替换为你的 appId
        String appKey = "rHHmLmBRaqsMMXnK"; // 替换为你的 appKey
        String URI = "/vivogpt/completions"; // 请求的 URI
        String DOMAIN = "api-ai.vivo.com.cn"; // 请求的域名
        String METHOD = "POST"; // HTTP 方法
        UUID requestId = UUID.randomUUID(); // 生成唯一的请求 ID
        System.out.println("requestId: " + requestId); // 打印请求 ID

        // 构建查询字符串
        Map<String, Object> map = new HashMap<>();
        map.put("requestId", requestId.toString()); // 将 requestId 添加到查询参数中
        String queryStr = mapToQueryString(map); // 将查询参数转换为查询字符串


        // 构建messages，包含多轮对话
        List<Map<String, String>> messages = loadHistory();
        
        // 添加历史对话
        // messages.add(createMessage("user", "你是谁？"));
        // messages.add(createMessage("assistant", "你好，我是蓝心小V，你的虚拟伙伴和闲聊好友。无论你心情如何，希望与你分享的话题有多么轻松或深奥，我都在这里随时准备和你聊上几句。所以，告诉我，今天的你，想要开始我们的对话从哪里呢？"));


        // 添加最新的用户输入
        messages.add(createMessage("user",question));


        // 构建请求体
        Map<String, Object> data = new HashMap<>();

        // data.put("prompt", "请告诉我你会什么？"); // 替换为你实际的 prompt

        data.put("messages", messages);
        data.put("model", "vivo-BlueLM-TB"); // 模型名称
        UUID sessionId = UUID.randomUUID(); // 生成唯一的会话 ID
        data.put("sessionId", sessionId.toString()); // 将 sessionId 添加到请求体中
        System.out.println(sessionId); // 打印会话 ID

        // 生成认证头
        HttpHeaders headers = VivoAuth.generateAuthHeaders(appId, appKey, METHOD, URI, queryStr); // 生成认证头
        headers.add("Content-Type", "application/json"); // 添加内容类型头
        System.out.println(headers); // 打印请求头

        // 构建请求 URL
        String url = String.format("http://%s%s?%s", DOMAIN, URI, queryStr); // 拼接 URL
        String requestBodyString = new ObjectMapper().writeValueAsString(data); // 将请求体转换为 JSON 字符串
        RestTemplate restTemplate = new RestTemplate(); // 创建 RestTemplate 实例
        HttpHeaders httpHeaders = new HttpHeaders(); // 创建新的 HttpHeaders 实例
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)); // 设置内容类型为 JSON
        httpHeaders.addAll(headers); // 将生成的认证头添加到 HttpHeaders 实例中
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyString, httpHeaders); // 构建 HttpEntity 实例，包含请求体和请求头

        // 发送 HTTP 请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class); // 发送 POST 请求并获取响应
        String assistantResponse="异常";
        if (response.getStatusCode() == HttpStatus.OK) { // 检查响应状态码
            System.out.println("Response body: " + response.getBody()); // 打印响应体

            // 从响应中提取 content
            assistantResponse = extractContentFromResponse(response.getBody());
            // 将 assistant 的回复添加到消息记录中
            messages.add(createMessage("assistant", assistantResponse));

            saveHistory(messages);//存储历史记录

        } else {
            assistantResponse="后台vivoApi响应状态异常";
            System.out.println("Error response: " + response.getStatusCode()); // 打印错误状态码
        }
        return assistantResponse; // 返回响应体
    }



    //输入role content，创建一个message 
    private Map<String, String> createMessage(String role, String content) {
        Map<String, String> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    //加载messages历史记录
    private List<Map<String, String>> loadHistory() throws IOException {
        List<Map<String, String>> messages = FileUtil.loadMessages();
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }
    //存储当前的回答
    private void saveHistory(List<Map<String, String>> messages) throws IOException {
        FileUtil.saveMessages(messages);
    }

    private String extractContentFromResponse(String jsonResponse) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode dataNode = root.path("data");
        return dataNode.path("content").asText();
    }

    // 将 Map 转换为查询字符串的方法
    public static String mapToQueryString(Map<String, Object> map) {
        if (map.isEmpty()) { // 如果 Map 为空，返回空字符串
            return "";
        }
        StringBuilder queryStringBuilder = new StringBuilder(); // 创建 StringBuilder 实例
        for (Map.Entry<String, Object> entry : map.entrySet()) { // 遍历 Map 中的每个条目
            if (queryStringBuilder.length() > 0) { // 如果 StringBuilder 中已有内容，添加 '&'
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append(entry.getKey()); // 添加键
            queryStringBuilder.append("="); // 添加 '='
            queryStringBuilder.append(entry.getValue()); // 添加值
        }
        return queryStringBuilder.toString(); // 返回构建的查询字符串
    }
}
