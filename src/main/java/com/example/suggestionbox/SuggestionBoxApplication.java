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
    private final String ADMIN_PASSWORD = "inputyourpassword";

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
                "<head>" +
                "  <title>4반 건의사항</title>" +
                "  <meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no'>" +
                "  <style>" +
                "    * { box-sizing: border-box; }" +
                "    body { font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif; margin: 0; padding: 15px; background-color: #f4f6f9; color: #333; line-height: 1.6; }" +
                "    .container { width: 100%; max-width: 700px; margin: 10px auto; background: white; padding: 35px 25px; border-radius: 16px; box-shadow: 0 6px 20px rgba(0,0,0,0.1); }" +
                "    h2 { font-size: 28px; margin-top: 0; margin-bottom: 25px; text-align: center; color: #2c3e50; font-weight: bold; }" +
                "    label { font-size: 18px; font-weight: bold; display: block; margin-bottom: 10px; color: #333; }" +
                "    input[type='text'], input[type='password'], textarea {" +
                "      width: 100%; padding: 18px; font-size: 18px; border: 2px solid #ddd; border-radius: 10px; margin-bottom: 25px; background-color: #fafafa; font-family: inherit;" +
                "    }" +
                "    input:focus, textarea:focus { border-color: #4CAF50; outline: none; background-color: #fff; }" +
                "    button { width: 100%; padding: 20px; font-size: 20px; font-weight: bold; border: none; border-radius: 10px; cursor: pointer; transition: background 0.2s; }" +
                "    .btn-submit { background-color: #4CAF50; color: white; margin-top: 10px; }" +
                "    .btn-admin { background-color: #007BFF; color: white; margin-top: 10px; }" +
                "    .admin-box { background: #f8f9fa; padding: 25px; border-radius: 12px; border: 1px solid #e9ecef; margin-top: 35px; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <h2>＼(︶ω︶)／ 4반 건의사항</h2>" +

                "    <form action='/suggest' method='post'>" +
                "      <label>학번 / 이름</label>" +
                "      <input type='text' name='studentInfo' placeholder='예: 40101 홍길동 or 익명' required>" +
                "      <label>건의 내용</label>" +
                "      <textarea name='content' rows='6' placeholder='건의할 내용을 자유롭게 적어주세요.' required></textarea>" +
                "      <button type='submit' class='btn-submit'>건의사항 제출하기</button>" +
                "    </form>" +

                "    <div class='admin-box'>" +
                "      <h3 style='margin-top:0; font-size:18px; color:#555;'>🔒 관리자 전용</h3>" +
                "      <form action='/list' method='post' style='margin-bottom:0;'>" +
                "        <input type='password' name='password' placeholder='비밀번호 입력' required style='margin-bottom:12px;'>" +
                "        <button type='submit' class='btn-admin'>목록 확인하기</button>" +
                "      </form>" +
                "    </div>" +
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
        return "<html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'></head>" +
                "<body style='font-family: Arial; padding: 40px 20px; text-align: center; line-height: 1.8;'>" +
                "  <h3 style='color: #4CAF50; font-size: 22px;'>✅ 건의사항이 DB에 안전하게 저장되었습니다!</h3>" +
                "  <br><a href='/' style='display: inline-block; padding: 16px 30px; background: #007BFF; color: white; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 18px;'>홈으로 돌아가기</a>" +
                "</body></html>";
    }

    // 건의사항 목록 조회 (DB에서 조회)
    @PostMapping(value = "/list", produces = "text/html;charset=UTF-8")
    public String listSuggestions(@RequestParam("password") String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return "<html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'></head>" +
                    "<body style='font-family: Arial; padding: 40px 20px; text-align: center;'>" +
                    "  <h3 style='color: red; font-size: 22px;'>❌ 비밀번호가 올바르지 않습니다!</h3>" +
                    "  <br><a href='/' style='color: #007BFF; font-weight: bold; font-size: 18px;'>홈으로 돌아가기</a>" +
                    "</body></html>";
        }

        List<Suggestion> suggestions = repository.findAll();

        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>4반 건의 목록</title>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<style>");
        html.append("  body { font-family: sans-serif; padding: 15px; margin: 0; background: #f9f9f9; }");
        html.append("  .table-container { overflow-x: auto; background: white; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); }");
        html.append("  table { width: 100%; border-collapse: collapse; min-width: 500px; }");
        html.append("  th, td { padding: 16px 12px; border: 1px solid #eee; text-align: left; font-size: 16px; }");
        html.append("  th { background-color: #007BFF; color: white; }");
        html.append("</style></head><body>");
        
        html.append("<h2 style='font-size: 24px;'>📋 4반 건의사항 목록 (총 ").append(suggestions.size()).append("건)</h2>");
        
        if (suggestions.isEmpty()) {
            html.append("<p style='font-size: 18px;'>아직 등록된 건의사항이 없습니다.</p>");
        } else {
            html.append("<div class='table-container'>");
            html.append("<table>");
            html.append("  <thead><tr>");
            html.append("    <th style='width: 60px; text-align: center;'>NO</th>");
            html.append("    <th style='width: 140px;'>학번/이름</th>");
            html.append("    <th>건의 내용</th>");
            html.append("  </tr></thead><tbody>");

            for (int i = 0; i < suggestions.size(); i++) {
                Suggestion s = suggestions.get(i);
                html.append("  <tr>");
                html.append("    <td style='text-align: center; font-weight: bold;'>").append(i + 1).append("</td>");
                html.append("    <td style='font-weight: bold;'>").append(s.getStudentInfo()).append("</td>");
                html.append("    <td style='white-space: pre-wrap;'>").append(s.getContent()).append("</td>");
                html.append("  </tr>");
            }

            html.append("  </tbody></table></div>");
        }
        
        html.append("<br><br><a href='/' style='display: block; text-align: center; padding: 16px; background: #6c757d; color: white; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 18px;'>⬅ 홈으로 돌아가기</a>");
        html.append("</body></html>");
        return html.toString();
    }
}
