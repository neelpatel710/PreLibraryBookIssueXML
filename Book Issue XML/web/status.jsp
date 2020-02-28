<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Status of Issued Book</title>
        <link rel="stylesheet" href="links.css">
    </head>
    <body>
        <%
            HttpSession hs = request.getSession(false);
            if(!hs.equals(null) && hs.getAttribute("username")==null){
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
                rd.forward(request,response);
            }
            String fname = (String)hs.getAttribute("first");
            String lname= (String)hs.getAttribute("last");
            String user_sess= (String)hs.getAttribute("username");
        %>
        
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
        <%@include file="header.html" %>
        <%@ page import="javax.xml.parsers.*" %>
        <%@ page import="org.w3c.dom.*" %>
        <%@ page import="java.io.*" %>
        <%@ page import="javax.xml.xpath.*" %>
        <%@ page import="java.util.*" %>
                
        <h1 class="name">Welcome, <c:out value="<%=fname%>"/> <c:out value="<%=lname%>"/></h1>
        <div class="middlebox">
            <form action="check_status" method="POST">
                <b>Enter Unique ID:</b> <input class="btn" type="text" name="user_id"/><br>
                <input class="btn btnsubstatus" type="submit" value="CHECK STATUS"/>    
            </form>
        </div>
    </body>
</html>