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
import java.util.logging.Logger;

@WebServlet(name = "helloServletnative", value = "/hello-servletnative")
public class HelloServletNativeQuery extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HelloServletNativeQuery.class.getSimpleName());

    @Inject
    private TestSessionBean session;

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        session.request1();

        List<String> list2 = session.requestNativeQuery();

        LOGGER.info("***** after Native Query request withException");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>list size: "+  "- " + list2.size() +  "</h1>");
        out.println("<a href=\"" + request.getContextPath()+ "/\">Back</a>");
        out.println("</body></html>");

    }

    public void destroy() {
    }
}