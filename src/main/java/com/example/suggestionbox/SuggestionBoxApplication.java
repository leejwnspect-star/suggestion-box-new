package com.example.suggestionbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class SuggestionBoxApplication {

    // 건의사항 데이터 저장 클래스
    static class Suggestion {
        String studentInfo;
        String content;

        Suggestion(String studentInfo, String content) {
            this.studentInfo = studentInfo;
            this.content = content;
        }
    }

    private final List<Suggestion> suggestions = new ArrayList<>();
    
    // 🔒 관리자 비밀번호 설정 (원하는 비밀번호로 수정하세요!)
    private final String ADMIN_PASSWORD = "1234";

    public static void main(String[] args) {
        SpringApplication.run(SuggestionBoxApplication.class, args);
    }

    // 1. 메인 건의 작성 화면
    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String home() {
        return "<html>" +
                "<head><title>4반 건의사항</title></head>" +
                "<body style='font-family: Arial, sans-serif; margin: 40px; line-height: 1.6; max-width: 600px;'>" +
                "  <h2> ＼(︶ω︶)／4반 건의사항</h2>" +
                "  <form action='/suggest' method='post' style='background: #f9f9f9; padding: 20px; border-radius: 8px; border: 1px solid #ddd;'>" +
                "    <label><b>학번 / 이름:</b></label><br>" +
                "    <input type='text' name='studentInfo' placeholder='예: 40101 홍길동 or 익명' required style='padding: 8px; width: 100%; margin-top: 5px; margin-bottom: 15px; box-sizing: border-box;'><br>" +
                "    <label><b>건의 내용:</b></label><br>" +
                "    <textarea name='content' rows='5' placeholder='건의할 내용을 자유롭게 적어주세요.' required style='padding: 8px; width: 100%; margin-top: 5px; margin-bottom: 15px; box-sizing: border-box;'></textarea><br>" +
                "    <button type='submit' style='padding: 10px 20px; background-color: #4CAF50; color: white; border: none; cursor: pointer; border-radius: 4px; font-weight: bold;'>제출하기</button>" +
                "  </form>" +
                "  <br><hr><br>" +
                "  <div style='background: #f0f4f8; padding: 15px; border-radius: 8px;'>" +
                "    <h3>🔒 관리자 전용!!</h3>" +
                "    <form action='/list' method='post'>" +
                "      <label>건의 목록 비밀번호:</label><br>" +
                "      <input type='password' name='password' placeholder='비밀번호 입력' required style='padding: 6px; margin-top: 5px; margin-bottom: 10px;'> " +
                "      <button type='submit' style='padding: 6px 12px; background-color: #007BFF; color: white; border: none; cursor: pointer; border-radius: 4px;'>목록 보기</button>" +
                "    </form>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    // 2. 건의사항 제출 처리
    @PostMapping(value = "/suggest", produces = "text/html;charset=UTF-8")
    public String addSuggestion(@RequestParam("studentInfo") String studentInfo, @RequestParam("content") String content) {
        if (content != null && !content.trim().isEmpty()) {
            suggestions.add(new Suggestion(studentInfo, content));
        }
        return "<html><body style='font-family: Arial; margin: 40px;'>" +
                "<h3>✅ 건의사항이 성공적으로 제출되었습니다!</h3>" +
                "<br><a href='/'>홈으로 돌아가기</a>" +
                "</body></html>";
    }

    // 3. 건의사항 목록 조회 (비밀번호 확인)
    @PostMapping(value = "/list", produces = "text/html;charset=UTF-8")
    public String listSuggestions(@RequestParam("password") String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return "<html><body style='font-family: Arial; margin: 40px; color: red;'>" +
                    "<h3>❌ 비밀번호가 올바르지 않습니다!</h3>" +
                    "<a href='/'>홈으로 돌아가기</a>" +
                    "</body></html>";
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial; margin: 40px; max-width: 700px;'>");
        html.append("<h2>📋 4반 건의사항 접수 목록 (총 ").append(suggestions.size()).append("건)</h2>");
        
        if (suggestions.isEmpty()) {
            html.append("<p>아직 등록된 건의사항이 없습니다.</p>");
        } else {
            html.append("<ul style='list-style-type: none; padding: 0;'>");
            for (int i = 0; i < suggestions.size(); i++) {
                Suggestion s = suggestions.get(i);
                html.append("<li style='background: #f9f9f9; border: 1px solid #ddd; padding: 15px; margin-bottom: 10px; border-radius: 6px;'>")
                    .append("<b>#").append(i + 1).append(" [").append(s.studentInfo).append("]</b><br>")
                    .append("<p style='margin-top: 5px; white-space: pre-wrap;'>").append(s.content).append("</p>")
                    .append("</li>");
            }
            html.append("</ul>");
        }
        
        html.append("<br><a href='/'>홈으로 돌아가기</a>");
        html.append("</body></html>");
        return html.toString();
    }
}
