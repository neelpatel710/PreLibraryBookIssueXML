package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class check_status extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        File issuedbookfile = new File("D:/Canada/WDM_PreLibraryBookIssue_Final/web/issuedbook.xml");
        String uid = request.getParameter("user_id");
        int i,book_flag=-1;
        String bname="",bpub="",day_r="",month_r="",year_r="";
        HttpSession hs = request.getSession(false);
        String uname = (String) hs.getAttribute("username");
        if(!uid.equals(""))
        {
            try
            {
                DocumentBuilderFactory dbuildfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dbuild = dbuildfactory.newDocumentBuilder();
                Document doc_status = dbuild.parse(issuedbookfile);
                
                //Actual Code Here
                NodeList all_status = doc_status.getElementsByTagName("book");
                for(i=0;i<all_status.getLength();i++)
                {
                    Element valid_record = (Element) all_status.item(i);
                    String username_check = valid_record.getElementsByTagName("userName").item(0).getTextContent();
                    String uid_check = valid_record.getAttribute("uid");
                    bname = valid_record.getElementsByTagName("bookName").item(0).getTextContent();
                    bpub = valid_record.getElementsByTagName("bookPub").item(0).getTextContent();
                    day_r = valid_record.getElementsByTagName("day_r").item(0).getTextContent();
                    month_r = valid_record.getElementsByTagName("month_r").item(0).getTextContent();
                    year_r = valid_record.getElementsByTagName("year_r").item(0).getTextContent();
                    if(username_check.equals(uname))
                    {
                        if(uid_check.equals(uid))
                        {
                            book_flag=0;
                            break;
                        }
                        else
                        {
                            book_flag=1;
                        }
                    }
                    else
                    {
                        book_flag=1;
                    }
                }
                if(book_flag==1)
                {
                    RequestDispatcher rd = request.getRequestDispatcher("status.jsp");
                    rd.include(request, response);
                    out.write("<div class='alert_status'>No such Book issued by You</div>");
                }
                else if(book_flag==0)
                {
                    RequestDispatcher rd = request.getRequestDispatcher("status.jsp");
                    rd.include(request, response);
                    out.write("<div class='alert_status' style='color:green'>Your Book Status is ISSUED. Collect it from Library<br>"
                            + "<b>Book Details:<br></b>"
                            + "Name: "+bname+"<br>"
                            + "Publisher: "+bpub+"<br>"
                            + "Collect Before: "+day_r+"-"+month_r+"-"+year_r+"</div>");
                }
            }
            catch(Exception e)
            {
                out.write(e.toString());
            }
        }
        else
        {
            RequestDispatcher rd = request.getRequestDispatcher("status.jsp");
            rd.include(request, response);
            out.write("<div class='alert_status'>Empty Fields</div>");
        }
    }
}
