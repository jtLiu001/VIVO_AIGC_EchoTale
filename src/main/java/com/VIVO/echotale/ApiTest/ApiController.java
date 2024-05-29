package com.VIVO.echotale.ApiTest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {

    @Autowired
    private VivoService vivoService;
    @GetMapping("/")
    public ResponseEntity<Resource> index() {
        Resource resource = new ClassPathResource("static/testpage.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }

    @PostMapping("/submit")
    @ResponseBody
    public String handleFormSubmit(@RequestBody Map<String, String> payload) {
        // 处理接收到的信息
        String message = payload.get("message");
        try {
            return "response:" + vivoService.vivogpt(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/submit_image")
    @ResponseBody
    public String handleFormSubmit_image(@RequestBody Map<String, Object> payload) {
        // 处理接收到的信息
        try {
            String task_id=vivoService.task_submit(payload);
            System.out.println("当前任务id是:"+task_id);
            String result="";
            while(true){
                final ObjectMapper objectMapper = new ObjectMapper();
                result=vivoService.taskProgress(task_id);
                JsonNode root = objectMapper.readTree(result);
                JsonNode dataNode = root.path("result");
                //如果任务完成，返回结果
                if(dataNode.path("finished").asText().equals("true")){
                    if (dataNode.has("images_url") && dataNode.get("images_url").isArray()) {
                        JsonNode imagesUrlNode = dataNode.get("images_url").get(0);
                        System.out.println("图片url: " + imagesUrlNode.asText());
                        return imagesUrlNode.asText();
                    } else {
                        System.out.println("图片url: 未找到");
                        return "未找到图片URL";
                    }
                }
                //等待两秒
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/test-vivodrawing")
    public String testVivodrawing() {
        try {
            String task_id=vivoService.task_submit();
            System.out.println("当前任务id是:"+task_id);
            String result="";
            while(true){
                final ObjectMapper objectMapper = new ObjectMapper();
                result=vivoService.taskProgress(task_id);
                JsonNode root = objectMapper.readTree(result);
                JsonNode dataNode = root.path("result");
                //如果任务完成，返回结果
                if(dataNode.path("finished").asText().equals("true")){
                    return result;
                }
                //等待两秒
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}