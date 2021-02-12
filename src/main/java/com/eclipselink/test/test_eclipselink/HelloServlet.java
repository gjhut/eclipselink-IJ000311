package com.eclipselink.test.test_eclipselink;

import com.eclipselink.test.session.TestSessionBean;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HelloServlet.class.getSimpleName());

    private String message;

    @Inject
    private TestSessionBean session;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        session.request1();

        List<String> list2 = session.requestNativeNamedQuery();

        LOGGER.info("***** after request withException");
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