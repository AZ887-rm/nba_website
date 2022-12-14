package service.user_modules;

import dao.DBQuery;
import swing.user_page.UserMainMenu;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class NewsModule {
    Connection conn;
    String username;

    public NewsModule(Connection conn, String username) {
        this.conn = conn;
        this.username = username;
    }

    public List<List<String>> getNews(int page) {
        try {
            CallableStatement cstmt;
            String sql;
            ResultSet rs;

            // using resultset to get every 10 rows in the table if exists
            sql = "CALL getNewsPage(?)";
            cstmt = conn.prepareCall(sql);
            cstmt.clearParameters();
            cstmt.setInt(1, page);
            rs = cstmt.executeQuery();

            List<List<String>> res = new LinkedList<>();
            while (rs.next()) {
                List<String> row = new LinkedList<>();
                row.add(rs.getString("topic"));
                row.add(rs.getString("title"));
                row.add(rs.getString("summary"));
                row.add(rs.getString("author"));
                row.add(rs.getString("publish_date"));
                row.add(rs.getString("link"));
                res.add(row);
            }

            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // display news module
    public void displayNews() {
        try {
            // get a statement
            Statement stmt = conn.createStatement();
            String sql;
            ResultSet rs = null;

            // using resultset to get every 10 rows in the table if exists
            int page = 0;
            int command;
            while (true) {
                if (page < 0) {
                    System.out.println("Reach the front");
                    page = 0;
                }

                sql = "SELECT * FROM news LIMIT " + page + ", 10";
                rs = DBQuery.getResultSet(conn, sql);

                // iterate the result
                System.out.println("page: " + (page + 1));
                System.out.printf("%-80s %-30s %-30s %-30s\n", "title", "author", "publish date", "topic");
                while (rs.next()) {
                    System.out.printf("%-80s %-30s %-30s %-30s\n", rs.getString("title"),
                            rs.getString("author"), rs.getString("publish_date"),
                            rs.getString("topic"));
                }
                System.out.println();

                System.out.println("1-Next page, 2-Second page, 3-back to main menu, other-quit");
                System.out.print("input: ");
                Scanner sc = new Scanner(System.in);
                command = sc.nextInt();

                if (command == 1)
                    page++;
                else if (command == 2)
                    page--;
                else if (command == 3)
                    break;
                else
                    break;
            }

            if (command == 3) {
                System.out.println();
                new UserMainMenu(conn, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> getMyNews(String username) {
        try {
            CallableStatement cstmt;
            String sql;
            ResultSet rs;
            sql = "CALL myOwnNews(?)";
            cstmt = conn.prepareCall(sql);
            cstmt.clearParameters();
            cstmt.setString(1, username);
            rs = cstmt.executeQuery();

            List<List<String>> res = new LinkedList<>();
            while (rs.next()) {
                List<String> row = new LinkedList<>();
                row.add(rs.getString("id"));
                row.add(rs.getString("topic"));
                row.add(rs.getString("title"));
                row.add(rs.getString("summary"));
                row.add(rs.getString("publish_date"));
                res.add(row);
            }

            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeNews(String topic, String title, String summary, String author, String publish_date) {
        try {
            CallableStatement cstmt;
            String sql;
            sql = "CALL writeMyNews(?, ?, ?, ?, ?)";
            cstmt = conn.prepareCall(sql);
            cstmt.clearParameters();
            cstmt.setString(1, topic);
            cstmt.setString(2, title);
            cstmt.setString(3, summary);
            cstmt.setString(4, author);
            cstmt.setString(5, publish_date);
            cstmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editTitle(String title, int id) {
        try {
            CallableStatement cstmt;
            String sql;
            sql = "CALL updateMyNewsTitle(?, ?)";
            cstmt = conn.prepareCall(sql);
            cstmt.clearParameters();
            cstmt.setString(1, title);
            cstmt.setInt(2, id);
            cstmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editSummary(String summary, int id) {
        try {
            CallableStatement cstmt;
            String sql;
            sql = "CALL updateMyNewsSummary(?, ?)";
            cstmt = conn.prepareCall(sql);
            cstmt.clearParameters();
            cstmt.setString(1, summary);
            cstmt.setInt(2, id);
            cstmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNews(int id) {
        try {
            CallableStatement cstmt;
            String sql;
            sql = "CALL deleteMyNews(?)";
            cstmt = conn.prepareCall(sql);
            cstmt.clearParameters();
            cstmt.setInt(1, id);
            cstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
