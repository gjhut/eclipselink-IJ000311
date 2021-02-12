package com.eclipselink.test.test_eclipselink;

import com.eclipselink.test.session.TestSessionBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "helloServletAll", value = "/hello-servletall")
public class HelloServletAll extends HttpServlet {
    private String message;

    @Inject
    private TestSessionBean session;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        List<String> list2 = session.requestAll();
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + ": "+  "- " + list2.size() +  "</h1>");
        out.println("<a href=\"" + request.getContextPath()+ "/\">Back</a>");
        out.println("</body></html>");

    }

    public void destroy() {
    }
}