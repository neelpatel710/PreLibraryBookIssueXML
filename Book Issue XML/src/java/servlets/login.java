package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;


public class login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        File authfile = new File("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/auth.xml");
        String uname = request.getParameter("username");
        String pass = request.getParameter("password");
        String fname=null;
        String lname=null;
        String password="";
        int flag=0;
        if(!uname.equals("") && !pass.equals(""))
        {
            try
            {
            DocumentBuilderFactory dbuildfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuild = dbuildfactory.newDocumentBuilder();
            Document doc = dbuild.parse(authfile);
            //out.write("Root Element: "+doc.getDocumentElement().getNodeName());
            NodeList alldata = doc.getElementsByTagName("member");
            for(int i=0;i<alldata.getLength();i++)
            {
                Node singledata = alldata.item(i);
                Element e = (Element)singledata;
                String user = e.getElementsByTagName("userName").item(0).getTextContent();
                fname = e.getElementsByTagName("firstName").item(0).getTextContent();
                lname = e.getElementsByTagName("lastName").item(0).getTextContent();
                password = e.getElementsByTagName("password").item(0).getTextContent();
                if (user.equals(uname))
                {
                    flag=1;
                    break;
                }
            }
            if(flag==1)
            {
                if(password.equals(pass))
                {
                    HttpSession hs = request.getSession(true);
                    hs.setAttribute("username",uname);
                    hs.setAttribute("first",fname);
                    hs.setAttribute("last",lname);
                    dbuildfactory = DocumentBuilderFactory.newInstance();
                    dbuild = dbuildfactory.newDocumentBuilder();
                    doc = dbuild.parse(new File("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/issuedbook.xml"));
                    
                    NodeList all = doc.getElementsByTagName("book");
                    Node parent = doc.getDocumentElement();
                    for(int i=0;i<all.getLength();i++)
                    {
                        Element single = (Element) all.item(i);
                        String username = single.getElementsByTagName("userName").item(0).getTextContent();
                        if(username.equals(uname))
                        {
                            Date sysdate = new Date();
                            String day_r = single.getElementsByTagName("day_r").item(0).getTextContent();
                            String month_r = single.getElementsByTagName("month_r").item(0).getTextContent();
                            String year_r = single.getElementsByTagName("year_r").item(0).getTextContent();
                            
                            String c_date = day_r+"-"+month_r+"-"+year_r;
                            Date collect_date = new SimpleDateFormat("dd-MM-yyyy").parse(c_date);
                            if(sysdate.after(collect_date) && !sysdate.equals(collect_date))
                            {   //out.write("Inside"+"\n");
                                //Deleting Node
                                parent.removeChild((Node)single);
  
                                //Node Deleted. Tranforming the file
                                TransformerFactory tfactory = TransformerFactory.newInstance();
                                Transformer trans = tfactory.newTransformer();
                                DOMSource src = new DOMSource(doc);
                                StreamResult result = new StreamResult("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/issuedbook.xml");
                                trans.transform(src, result);
                            }
                        }
                    }
                    
                    RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
                    rd.include(request, response);
                }
                else
                {
                    RequestDispatcher rd = request.getRequestDispatcher("index.html");
                    rd.include(request, response);
                    out.write("<div class='alert'>Password Incorrect</div>");
                }
            }
            else if(flag==0)
            {
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
                out.write("<div class='alert'>Username does not Exists</div>");   
            }
            }
            catch(Exception e)
            {
                out.write(e.toString());
            }
        }
        else
        {
            RequestDispatcher rd = request.getRequestDispatcher("index.html");
            rd.include(request, response);
            out.write("<div class='alert'>Empty Fields</div>");
        }
    }
}
