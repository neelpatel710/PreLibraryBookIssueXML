<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your List</title>
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
            String user_sess = (String) hs.getAttribute("username");
        %>
        
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
        <%@include file="header.html" %>
        <%@ page import="javax.xml.parsers.*" %>
        <%@ page import="org.w3c.dom.*" %>
        <%@ page import="java.io.*" %>
        <%@ page import="javax.xml.xpath.*" %>
        <%@ page import="javax.xml.transform.*" %>
        <%@ page import="javax.xml.transform.stream.*" %>
                
        <h1 class="name">Welcome, <c:out value="<%=fname%>"/> <c:out value="<%=lname%>"/></h1>
        <div class="middlebox">
            <c:import var="xmlfile" url="issuedbook.xml"/>
            <c:import var="xslfile" url="yourbookdetails.xsl"/>
            <x:transform xml="${xmlfile}" xslt="${xslfile}">
                <x:param name="user" value="<%=user_sess%>"/>
            </x:transform>
        </div>
    </body>
</html>