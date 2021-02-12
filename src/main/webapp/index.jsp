<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<ul>
<li><a href="hello-servlet">Native named query, with stacktrace</a></li>
<li><a href="hello-servletnative">native direct query, with stacktrace</a></li>
<br />
<li><a href="hello-servlet2">Directly called named query, from CDI-bean, no stacktrace</a></li>
<li><a href="hello-servletall">All in one transaction, no stacktrace</a></li>
<li><a href="hello-servletjpanamed">jpa named query, no stacktrace</a></li>
<li><a href="hello-servletdirectdao">DAO EJB directly called, no stacktrace</a></li>
</ul>
</body>
</html>