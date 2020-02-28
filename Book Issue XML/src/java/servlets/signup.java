package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class signup extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String pw1 = request.getParameter("pwd1");
        String pw2 = request.getParameter("pwd2");
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("emailid");
        String user_check = request.getParameter("username");
        int flag=0;
        if(!fname.equals("") && !lname.equals("") && !user_check.equals("") && !pw1.equals("") && !pw2.equals(""))
        {
            if(pw1.equals(pw2))
            {
                try
                {
                    File authfile = new File("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/auth.xml"); 
                    DocumentBuilderFactory dbuildfactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dbuild = dbuildfactory.newDocumentBuilder();
                    Document doc = dbuild.parse(authfile);
                    Element rootNode = doc.getDocumentElement();
                    
                    NodeList all = doc.getElementsByTagName("member");
                    for(int i=0; i<all.getLength();i++)
                    {
                        Node single = all.item(i);
                        Element e = (Element) single;
                        String user = e.getElementsByTagName("userName").item(0).getTextContent();
                        if(user.equals(user_check))
                        {
                            flag=1;
                            break;
                        }
                    }
                    if(flag==1)
                    {
                        RequestDispatcher rd = request.getRequestDispatcher("register.html");
                        rd.include(request,response);
                        out.write("<div class='alert'>Username already exists</div>");
                    }
                    else if(flag==0)
                    {
                        //Adding New Member Details
                        Element newmember = doc.createElement("member");
                        rootNode.appendChild(newmember);

                        //Inserting First Name
                        Element firstname = doc.createElement("firstName");
                        firstname.appendChild(doc.createTextNode(fname));
                        newmember.appendChild(firstname);

                        //Inserting Last Name
                        Element lastname = doc.createElement("lastName");
                        lastname.appendChild(doc.createTextNode(lname));
                        newmember.appendChild(lastname);

                        //Inserting Email ID
                        Element emailID = doc.createElement("emailID");
                        emailID.appendChild(doc.createTextNode(email));
                        newmember.appendChild(emailID);

                        //Inserting Username
                        Element username = doc.createElement("userName");
                        username.appendChild(doc.createTextNode(user_check));
                        newmember.appendChild(username);

                        //Inserting Password
                        Element pass = doc.createElement("password");
                        pass.appendChild(doc.createTextNode(pw1));
                        newmember.appendChild(pass);
                        
                        TransformerFactory tfactory = TransformerFactory.newInstance();
                        Transformer trans = tfactory.newTransformer();
                        DOMSource src = new DOMSource(doc);
                        StreamResult result = new StreamResult("D:/Canada/WDM/WDM_PreLibraryBookIssue_Final/web/auth.xml");
                        trans.transform(src, result);
                                
                        RequestDispatcher rd = request.getRequestDispatcher("register.html");
                        rd.include(request,response);
                        out.write("<div class='alert'>Registered Succesfully (<a style='color:white'href='index.html'><b>Login</b></a>)</div>");
                    }
                }
                catch(Exception e)
                {
                    out.write(e.toString());
                }
            }
            else
            {
                RequestDispatcher rd = request.getRequestDispatcher("register.html");
                rd.include(request,response);
                out.write("<div class='alert'>Password Mismatch</div>");
            }
        }
        else
        {
            RequestDispatcher rd = request.getRequestDispatcher("register.html");
            rd.include(request,response);
            out.write("<div class='alert'>Empty Fields</div>");
        }
    }
}
