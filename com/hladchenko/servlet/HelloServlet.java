package com.hladchenko.servlet;

import jakarta.servlet.ServletException;
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

    private static final String SESSION_ID = "SESSION_ID";

    private static final Map<UUID, String> SESSION_STORAGE = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nameParameter = req.getParameter("name");
        String name = nameParameter != null ? nameParameter : "";
        Cookie sessionCookie = null;
        Cookie[] cookies = req.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_ID)) {
                sessionCookie = cookie;
            }
        }

        if (sessionCookie != null) {
            String cookieName = sessionCookie.getName();
            String cookieValue = SESSION_STORAGE.get(UUID.fromString(cookieName));
            if (cookieValue != null) {
                name = cookieValue;
            }
        } else {
            UUID currentSessionId = UUID.randomUUID();
            sessionCookie = new Cookie(currentSessionId.toString(), name);
            SESSION_STORAGE.put(currentSessionId, name);
        }

        String greeting = name.isEmpty() ? "Hello" : String.format("Hello, %s!", name);

        var writer = new PrintWriter(resp.getOutputStream());
        writer.write(greeting);
        writer.flush();

        resp.addCookie(sessionCookie);
    }
}
