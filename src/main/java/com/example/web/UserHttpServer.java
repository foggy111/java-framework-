package com.example.web;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserHttpServer {

    private static SqlSessionFactory factory;

    public static void main(String[] args) throws Exception {
        factory = new SqlSessionFactoryBuilder()
                .build(Resources.getResourceAsStream("mybatis-config.xml"));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/user/findAll", UserHttpServer::findAll);
        server.createContext("/user/findById", UserHttpServer::findById);
        server.start();
        System.out.println("HTTP 已启动: http://localhost:8080/user/findAll");

    }

    private static void findAll(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            write(exchange, 405, "{\"code\":405,\"message\":\"只支持GET\"}");
            return;
        }
        try (SqlSession session = factory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> users = mapper.findAll();
            write(exchange, 200, toResult(users));
        } catch (Exception e) {
            e.printStackTrace();
            write(exchange, 500, "{\"code\":500,\"message\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    private static void findById(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int id = 1;
        if (query != null && query.startsWith("id=")) {
            id = Integer.parseInt(query.substring(3));
        }
        try (SqlSession session = factory.openSession()) {
            User user = session.getMapper(UserMapper.class).findById(id);
            write(exchange, 200, "{\"code\":200,\"message\":\"查询成功\",\"data\":" + toJson(user) + "}");
        } catch (Exception e) {
            e.printStackTrace();
            write(exchange, 500, "{\"code\":500,\"message\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    private static String toResult(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"code\":200,\"message\":\"查询成功\",\"data\":[");
        for (int i = 0; i < users.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(toJson(users.get(i)));
        }
        sb.append("]}");
        return sb.toString();
    }

    private static String toJson(User user) {
        if (user == null) {
            return "null";
        }
        return "{\"id\":" + user.getId()
                + ",\"username\":\"" + escape(user.getUsername()) + "\""
                + ",\"email\":\"" + escape(user.getEmail()) + "\"}";
    }

    private static void write(HttpExchange exchange, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}