package com.example.suggestionbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class SuggestionBoxApplication {

    private final SuggestionRepository repository;
    
    // 🔒 관리자 비밀번호
    private final String ADMIN_PASSWORD = "1234";

    public SuggestionBoxApplication(SuggestionRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SuggestionBoxApplication.class, args);
    }

    // 메인 건의 작성 화면
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

    // 건의사항 제출 (DB 저장)
    @PostMapping(value = "/suggest", produces = "text/html;charset=UTF-8")
    public String addSuggestion(@RequestParam("studentInfo") String studentInfo, @RequestParam("content") String content) {
        if (content != null && !content.trim().isEmpty()) {
            repository.save(new Suggestion(studentInfo, content));
        }
        return "<html><body style='font-family: Arial; margin: 40px;'>" +
                "<h3>✅ 건의사항이 DB에 안전하게 저장되었습니다!</h3>" +
                "<br><a href='/'>홈으로 돌아가기</a>" +
                "</body></html>";
    }

    // 건의사항 목록 조회 (DB에서 조회)
    @PostMapping(value = "/list", produces = "text/html;charset=UTF-8")
    public String listSuggestions(@RequestParam("password") String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return "<html><body style='font-family: Arial; margin: 40px; color: red;'>" +
                    "<h3>❌ 비밀번호가 올바르지 않습니다!</h3>" +
                    "<a href='/'>홈으로 돌아가기</a>" +
                    "</body></html>";
        }

        List<Suggestion> suggestions = repository.findAll();

        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>4반 건의 목록</title></head>");
        html.append("<body style='font-family: Arial, sans-serif; margin: 40px; max-width: 800px;'>");
        html.append("<h2>📋 4반 건의사항 접수 목록 (총 ").append(suggestions.size()).append("건)</h2>");
        
        if (suggestions.isEmpty()) {
            html.append("<p>아직 등록된 건의사항이 없습니다.</p>");
        } else {
            html.append("<table style='width: 100%; border-collapse: collapse; margin-top: 20px; text-align: left;'>");
            html.append("  <thead>");
            html.append("    <tr style='background-color: #007BFF; color: white;'>");
            html.append("      <th style='padding: 10px; border: 1px solid #ddd; width: 60px; text-align: center;'>번호</th>");
            html.append("      <th style='padding: 10px; border: 1px solid #ddd; width: 160px;'>학번 / 이름</th>");
            html.append("      <th style='padding: 10px; border: 1px solid #ddd;'>건의 내용</th>");
            html.append("    </tr>");
            html.append("  </thead>");
            html.append("  <tbody>");

            for (int i = 0; i < suggestions.size(); i++) {
                Suggestion s = suggestions.get(i);
                String bgColor = (i % 2 == 0) ? "#ffffff" : "#f9f9f9";
                
                html.append("    <tr style='background-color: ").append(bgColor).append(";'>");
                html.append("      <td style='padding: 10px; border: 1px solid #ddd; text-align: center; font-weight: bold;'>").append(i + 1).append("</td>");
                html.append("      <td style='padding: 10px; border: 1px solid #ddd; font-weight: bold;'>").append(s.getStudentInfo()).append("</td>");
                html.append("      <td style='padding: 10px; border: 1px solid #ddd; white-space: pre-wrap;'>").append(s.getContent()).append("</td>");
                html.append("    </tr>");
            }

            html.append("  </tbody>");
            html.append("</table>");
        }
        
        html.append("<br><br><a href='/' style='text-decoration: none; color: #007BFF; font-weight: bold;'>⬅ 홈으로 돌아가기</a>");
        html.append("</body></html>");
        return html.toString();
    }
}
