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


    @GetMapping("/test-vivogpt")
    public String testVivogpt() {
        try {
            return vivoService.vivogpt();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}