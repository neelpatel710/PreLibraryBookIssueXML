package servlets;

import java.io.*;
import java.text.SimpleDateFormat;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.util.*;


public class issue extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        File bookdetailfile = new File("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/bookdetails.xml");
        File issuedbookfile = new File("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/issuedbook.xml");
        String dept = request.getParameter("dept");
        String bname = request.getParameter("bname");
        String bpub = request.getParameter("bpub");
        String r_date = request.getParameter("r_date");
        int bpub_error=-1;
        int bname_error=-1;
        int new_issue=-1;
        int avail_check=-1;
        DocumentBuilderFactory dbuildfactory;
        DocumentBuilder dbuild;
        Document doc_check;
        NodeList all;
        int i,j;
        HttpSession hs = request.getSession(false);
        String uname = (String) hs.getAttribute("username");
        if(!dept.equals("Select Department") && !bname.equals("Select Book") &&!bpub.equals("Select Publisher") &&!r_date.equals("yyyy-mm-dd"))
        {
            try
            {
                dbuildfactory = DocumentBuilderFactory.newInstance();
                dbuild = dbuildfactory.newDocumentBuilder();
                doc_check = dbuild.parse(bookdetailfile);
                Document doc_insert = dbuild.parse(issuedbookfile);

                //Checking Details
                all = doc_check.getElementsByTagName("books");
                for(i=0;i<all.getLength();i++)
                {
                    Element e = (Element) all.item(i);
                    String dept_check = e.getElementsByTagName("dept").item(0).getTextContent();
                    String bname_check = e.getElementsByTagName("bName").item(0).getTextContent();
                    String bpub_check = e.getElementsByTagName("bPublisher").item(0).getTextContent();
                    int avail = Integer.parseInt(e.getElementsByTagName("avail").item(0).getTextContent());
                    if(dept_check.equals(dept))
                    {
                        if(bname_check.equals(bname))
                        {
                            bname_error=0;
                            if(bpub_check.equals(bpub))
                            {
                                bpub_error=0;
                                if(avail>0)
                                {
                                    avail_check=0;
                                    break;
                                }
                            }
                            else
                            {
                                bpub_error=1;

                            }
                        }
                        else
                        {
                            bname_error=1;

                        }
                    }
                }
                if(avail_check==0)
                {
                    Document doc_already = dbuild.parse("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/issuedbook.xml");
                    NodeList all_already = doc_already.getElementsByTagName("book");
                    for(i=0;i<all_already.getLength();i++)
                    {
                        Element e = (Element) all_already.item(i);
                        String userName = e.getElementsByTagName("userName").item(0).getTextContent();
                        String bookName = e.getElementsByTagName("bookName").item(0).getTextContent();
                        String bookPub = e.getElementsByTagName("bookPub").item(0).getTextContent();
                        if(userName.equals(uname) && bookName.equals(bname) && bookPub.equals(bpub))
                        {
                            new_issue=0;
                            break;
                        }                        
                    }
                    if(new_issue==0)
                    {
                        RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
                        rd.include(request, response);
                        out.write("<div class='response'>Book already issued. Check 'Book Issued' tab</div>");
                    }
                }
                if(avail_check==0 && new_issue!=0)
                {
                    Date sysdate = new Date();
                    String year_i = new SimpleDateFormat("yyyy",Locale.ENGLISH).format(sysdate);
                    String month_i = new SimpleDateFormat("MM",Locale.ENGLISH).format(sysdate);
                    String day_i = new SimpleDateFormat("dd",Locale.ENGLISH).format(sysdate);

                    Date return_date = new SimpleDateFormat("yyyy-MM-dd").parse(r_date); 
                    String year_r = new SimpleDateFormat("yyyy",Locale.ENGLISH).format(return_date);
                    String month_r = new SimpleDateFormat("MM",Locale.ENGLISH).format(return_date);
                    String day_r = new SimpleDateFormat("dd",Locale.ENGLISH).format(return_date);
                    
                    if(sysdate.compareTo(return_date)<0 && sysdate.compareTo(return_date)!=0)
                    {
                        NodeList all_issue = doc_insert.getElementsByTagName("book");

                        Element e1 = (Element) all_issue.item(all_issue.getLength()-1);
                        int uid_last = Integer.parseInt(e1.getAttribute("uid"));
                        uid_last = uid_last+1;
                        String uid = Integer.toString(uid_last);

                        Element rootNode = doc_insert.getDocumentElement();

                        Element booknode = doc_insert.createElement("book");
                        booknode.setAttribute("uid", uid);
                        rootNode.appendChild(booknode);

                        Element ndept = doc_insert.createElement("dept");
                        ndept.appendChild(doc_insert.createTextNode(dept));
                        booknode.appendChild(ndept);

                        Element userName = doc_insert.createElement("userName");
                        userName.appendChild(doc_insert.createTextNode(uname));
                        booknode.appendChild(userName);

                        Element bookName = doc_insert.createElement("bookName");
                        bookName.appendChild(doc_insert.createTextNode(bname));
                        booknode.appendChild(bookName);

                        Element bookpub = doc_insert.createElement("bookPub");
                        bookpub.appendChild(doc_insert.createTextNode(bpub));
                        booknode.appendChild(bookpub);

                        Element nday_i = doc_insert.createElement("day_i");
                        nday_i.appendChild(doc_insert.createTextNode(day_i));
                        booknode.appendChild(nday_i);

                        Element nmonth_i = doc_insert.createElement("month_i");
                        nmonth_i.appendChild(doc_insert.createTextNode(month_i));
                        booknode.appendChild(nmonth_i);

                        Element nyear_i = doc_insert.createElement("year_i");
                        nyear_i.appendChild(doc_insert.createTextNode(year_i));
                        booknode.appendChild(nyear_i);

                        Element nday_r = doc_insert.createElement("day_r");
                        nday_r.appendChild(doc_insert.createTextNode(day_r));
                        booknode.appendChild(nday_r);

                        Element nmonth_r = doc_insert.createElement("month_r");
                        nmonth_r.appendChild(doc_insert.createTextNode(month_r));
                        booknode.appendChild(nmonth_r);

                        Element nyear_r = doc_insert.createElement("year_r");
                        nyear_r.appendChild(doc_insert.createTextNode(year_r));
                        booknode.appendChild(nyear_r);

                        TransformerFactory tfactory = TransformerFactory.newInstance();
                        Transformer trans = tfactory.newTransformer();
                        DOMSource src = new DOMSource(doc_insert);

                        StreamResult result = new StreamResult("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/issuedbook.xml");
                        trans.transform(src, result);
                        
                        //Updating the book details file availability
                        for(i=0;i<all.getLength();i++)
                        {
                            Element e = (Element) all.item(i);
                            String dept_check = e.getElementsByTagName("dept").item(0).getTextContent();
                            String bname_check = e.getElementsByTagName("bName").item(0).getTextContent();
                            String bpub_check = e.getElementsByTagName("bPublisher").item(0).getTextContent();
                            int avail_inc = Integer.parseInt(e.getElementsByTagName("avail").item(0).getTextContent());
                            if(dept_check.equals(dept) && bname_check.equals(bname) && bpub_check.equals(bpub))
                            {
                                avail_inc = avail_inc-1;
                                Node ne = (Node) all.item(i);
                                NodeList child = ne.getChildNodes();
                                for(j=0;j<child.getLength();j++)
                                {
                                    Node child_fetch = (Node) child.item(j);
                                    if((child_fetch.getNodeName()).equals("avail"))
                                    {
                                        child_fetch.setTextContent(Integer.toString(avail_inc));
                                        break;
                                    }
                                }
                            }
                        }
                        
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc_check);
                        
                        result = new StreamResult("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/bookdetails.xml");
                        transformer.transform(source, result);
                
                        RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
                        rd.include(request, response);
                        out.write("<div class='response' style='color:green'>Book issued successfully</div>");
                    }
                    else
                    {
                        RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
                        rd.include(request, response);
                        out.write("<div class='response'>Select Appropriate Date</div>");
                    }
                }
                else if(i==all.getLength())
                {
                    if(bname_error==1)
                    {
                        RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
                        rd.include(request, response);
                        out.write("<div class='response'>Book not present in Department Selected</div>");
                    }
                    if(bname_error==0 && bpub_error==1)
                    {
                        RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
                        rd.include(request, response);
                        out.write("<div class='response'>Wrong Publisher Selected</div>");
                    }
                    if(bname_error==0 && bpub_error==0 && avail_check!=0)
                    {
                        RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
                        rd.include(request, response);
                        out.write("<div class='response'>Book Not Available. Try after few days</div>");
                    }
                }
            }
            catch(Exception e)
            {
                out.write(e.toString());
            }
        }
        else
        {
            RequestDispatcher rd = request.getRequestDispatcher("issue_book.jsp");
            rd.include(request, response);
            out.write("<div class='response'>Empty Fields</div>");
        }
    }
}
