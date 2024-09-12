package com.techcourse.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.model.User;
import java.io.ByteArrayInputStream;
import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;
import org.apache.coyote.http11.HttpHeaders;
import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginServletTest {

    @Test
    @DisplayName("세션이 유효한 경우, 홈 페이지로 리다이렉트된다.")
    void redirectOnValidSession() {
        SessionManager manager = new SessionManager();
        Session session = new Session("hoony");
        session.setAttribute("user", new User("aru", "dong", "hoony"));
        manager.add(session);

        LoginServlet handler = new LoginServlet();
        byte[] requestBytes = """
                GET /login HTTP/1.1\r
                Cookie: JSESSIONID=hoony\r
                """.getBytes();
        HttpRequest request = new HttpRequest(new ByteArrayInputStream(requestBytes));

        request.setManager(manager);
        HttpResponse response = new HttpResponse();
        handler.service(request, response);
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo("/index.html");

        manager.remove(session);
    }

    @Test
    @DisplayName("세션이 유효하지 않은 경우, 로그인 페이지를 불러온다.")
    void loadOnInvalidSession() {
        SessionManager manager = new SessionManager();
        LoginServlet handler = new LoginServlet();
        byte[] requestBytes = """
                GET /login HTTP/1.1\r
                Cookie: JSESSIONID=hoony\r
                """.getBytes();
        HttpRequest request = new HttpRequest(new ByteArrayInputStream(requestBytes));

        request.setManager(manager);
        HttpResponse response = new HttpResponse();
        handler.service(request, response);
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo("/login.html");
    }

    @Test
    @DisplayName("세션이 존재하지 않는 경우, 로그인 페이지를 불러온다.")
    void loadOnNoSession() {
        SessionManager manager = new SessionManager();
        LoginServlet handler = new LoginServlet();
        byte[] requestBytes = """
                GET /login HTTP/1.1\r
                """.getBytes();
        HttpRequest request = new HttpRequest(new ByteArrayInputStream(requestBytes));

        request.setManager(manager);
        HttpResponse response = new HttpResponse();
        handler.service(request, response);
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo("/login.html");
    }
}
