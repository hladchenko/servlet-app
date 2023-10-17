package com.hladchenko.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    public static final String SESSION_ID = "SESSION_ID";

    public static final Map<String, String> map = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");

        Cookie sessionCookie = null;

        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SESSION_ID)) {
                    sessionCookie = cookie;
                    name = map.get(sessionCookie.getValue());
                }
            }
        }

        if (sessionCookie == null && name != null) {
            String id = UUID.randomUUID().toString();
            sessionCookie = new Cookie(SESSION_ID, id);
            map.put(id, name);
        }

        if (sessionCookie != null) {
            resp.addCookie(sessionCookie);
        }

        String greeting = name == null ? "Hello!" : String.format("Hello, %s!", name);

        ServletOutputStream outputStream = resp.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write(greeting);
        printWriter.flush();
    }
}
