<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Issue Book</title>
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
            <form action="issue" method="POST">
                <b>Select Department:</b> 
                <select class="btn" name="dept"> 
                    <option selected>Select Department</option>
                    <% 
                    DocumentBuilderFactory dbuildfactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dbuild = dbuildfactory.newDocumentBuilder();
                    Document doc = dbuild.parse(new File("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/bookdetails.xml"));
                    
                    NodeList all = doc.getElementsByTagName("books");
                    ArrayList<String> arr = new ArrayList();
                    
                    for(int i=0; i<all.getLength();i++)
                    {
                        Element e = (Element) all.item(i);
                        String dept = e.getElementsByTagName("dept").item(0).getTextContent();
                        if(arr.contains(dept))
                        {
                            continue;
                        }
                        else
                        {
                            arr.add(dept);
                        }
                    }
                    for(int i=0;i<arr.size();i++)
                    {
                        out.write("<option value='"+arr.get(i)+"'>"+arr.get(i)+"</option>");
                    }
                    %>
                </select><br><br>
                <b>Book Name:</b> 
                <select class="btn" name="bname"> 
                    <option selected>Select Book</option>
                <%  
                    all = doc.getElementsByTagName("books");
                    arr = new ArrayList();
                    
                    for(int i=0; i<all.getLength();i++)
                    {
                        Element e = (Element) all.item(i);
                        String bname = e.getElementsByTagName("bName").item(0).getTextContent();
                        if(arr.contains(bname))
                        {
                            continue;
                        }
                        else
                        {
                            arr.add(bname);
                        }
                    }
                    for(int i=0;i<arr.size();i++)
                    {
                        out.write("<option value='"+arr.get(i)+"'>"+arr.get(i)+"</option>");
                    }
                %>
                </select><br><br>
                <b>Publisher Name:</b> 
                <select class="btn" name="bpub"> 
                    <option selected>Select Publisher</option>
                    <% 
                    all = doc.getElementsByTagName("books");
                    arr = new ArrayList();
                    
                    for(int i=0; i<all.getLength();i++)
                    {
                        Element e = (Element) all.item(i);
                        String bpub = e.getElementsByTagName("bPublisher").item(0).getTextContent();
                        if(arr.contains(bpub))
                        {
                            continue;
                        }
                        else
                        {
                            arr.add(bpub);
                        }
                    }
                    for(int i=0;i<arr.size();i++)
                    {
                        out.write("<option value='"+arr.get(i)+"'>"+arr.get(i)+"</option>");
                    }
                    %>
                </select><br><br>
                
                <b>Collect Before: </b><input class="btn" name="r_date" type="date"><br>
                
                <input class="btn btnsub" type="submit" value="BOOK NOW"/>    
            </form>
        </div>
    </body>
</html>