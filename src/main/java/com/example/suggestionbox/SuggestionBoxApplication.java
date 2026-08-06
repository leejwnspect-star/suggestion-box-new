package com.example.suggestionbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class SuggestionBoxApplication {

    private final List<String> suggestions = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(SuggestionBoxApplication.class, args);
    }

    // 건의사항 작성 웹 화면
    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String home() {
        return "<html>" +
                "<head><title>익명 건의함</title></head>" +
                "<body style='font-family: Arial, sans-serif; margin: 40px; line-height: 1.6;'>" +
                "  <h2>📬 익명 건의함</h2>" +
                "  <form action='/suggest' method='post' style='margin-bottom: 20px;'>" +
                "    <textarea name='content' rows='4' cols='50' placeholder='건의할 내용을 입력하세요...' required style='padding: 10px; width: 100%; max-width: 500px;'></textarea><br><br>" +
                "    <button type='submit' style='padding: 10px 20px; background-color: #4CAF50; color: white; border: none; cursor: pointer; border-radius: 4px;'>건의하기</button>" +
                "  </form>" +
                "  <hr>" +
                "  <h3>📋 접수된 건의 목록</h3>" +
                "  <a href='/list' style='text-decoration: none; color: #007BFF;'>👉 등록된 건의사항 전체보기</a>" +
                "</body>" +
                "</html>";
    }

    // 건의사항 제출 처리
    @PostMapping(value = "/suggest", produces = "text/html;charset=UTF-8")
    public String addSuggestion(@RequestParam("content") String content) {
        if (content != null && !content.trim().isEmpty()) {
            suggestions.add(content);
        }
        return "<html><body style='font-family: Arial; margin: 40px;'>" +
                "<h3>✅ 건의사항이 정상적으로 등록되었습니다!</h3>" +
                "<a href='/'>홈으로 돌아가기</a> | <a href='/list'>건의 목록 보기</a>" +
                "</body></html>";
    }

    // 건의사항 목록 조회
    @GetMapping(value = "/list", produces = "text/html;charset=UTF-8")
    public String listSuggestions() {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial; margin: 40px;'>");
        html.append("<h2>📋 접수된 건의 목록 (총 ").append(suggestions.size()).append("건)</h2>");
        
        if (suggestions.isEmpty()) {
            html.append("<p>아직 등록된 건의사항이 없습니다.</p>");
        } else {
            html.append("<ol>");
            for (String item : suggestions) {
                html.append("<li style='margin-bottom: 8px;'>").append(item).append("</li>");
            }
            html.append("</ol>");
        }
        
        html.append("<br><a href='/'>홈으로 돌아가기</a>");
        html.append("</body></html>");
        return html.toString();
    }
}
