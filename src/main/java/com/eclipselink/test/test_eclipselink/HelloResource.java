package com.eclipselink.test.test_eclipselink;

import com.eclipselink.test.session.TestSessionBean;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/hello-world")
public class HelloResource {


    @Inject
    TestSessionBean session;

    @GET
    @Produces("text/plain")
    public String hello() {
        List<String> list = session.requestNativeNamedQuery();

        return "Hello, World!"+ list.size();
    }
}