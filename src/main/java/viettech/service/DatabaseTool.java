package viettech.service;

import dev.langchain4j.agent.tool.Tool;
import java.sql.*;

public class DatabaseTool {

    // THAY THÔNG TIN DB AIVEN CỦA BẠN VÀO ĐÂY
    private final String DB_URL = System.getenv("DB_URL");
    private final String USER = System.getenv("DB_USER");
    private final String PASS = System.getenv("DB_PASSWORD");

    @Tool("Executes a SELECT SQL query on the MySQL database and returns the result rows.")
    public String executeQuery(String query) {
        // Chỉ cho phép SELECT để an toàn
        if (!query.trim().toUpperCase().startsWith("SELECT")) {
            return "Error: Only SELECT queries are allowed.";
        }

        StringBuilder sb = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int colCount = rs.getMetaData().getColumnCount();

            // Lấy tiêu đề cột
            for (int i = 1; i <= colCount; i++) {
                sb.append(rs.getMetaData().getColumnName(i)).append("\t | ");
            }
            sb.append("\n---\n");

            // Lấy dữ liệu
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                for (int i = 1; i <= colCount; i++) {
                    sb.append(rs.getString(i)).append("\t | ");
                }
                sb.append("\n");
            }

            if (!hasData) return "Query successful but returned no data.";

        } catch (SQLException e) {
            return "Database Error: " + e.getMessage();
        }
        return sb.toString();
    }
}