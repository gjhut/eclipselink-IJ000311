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

@WebServlet(name = "helloServletjpanamed", value = "/hello-servletjpanamed")
public class HelloServletJpaNamedQuery extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HelloServletJpaNamedQuery.class.getSimpleName());

    @Inject
    private TestSessionBean session;

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        session.request1();

        List<String> list2 = session.requestJpaNamedQuery();

        LOGGER.info("***** after JpaNamedQuery request ");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>list size: "+  "- " + list2.size() +  "</h1>");
        out.println("<a href=\"" + request.getContextPath()+ "/\">Back</a>");
        out.println("</body></html>");

    }

    public void destroy() {
    }
}