

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ConfirmPage
 */

public class ConfirmPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfirmPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 PrintWriter out = response.getWriter();
		 HttpSession session = request.getSession();
		 Object loggedIn = session.getAttribute("LoginInPage");
		 if (loggedIn != null && (Integer) loggedIn == 1) {
		 session = request.getSession(true);
		 //Integer LogInPage = (Integer) session.getAttribute("LogInPage");
			 ArrayList <String> movieItems = null;
			    synchronized(session) {
			        movieItems = (ArrayList<String>)session.getAttribute("movieItems");
			        if (movieItems == null) {
			                movieItems = new ArrayList<String>();
			                session.setAttribute("movieItems", movieItems);
			        }
			 }
			    String title = "Items purchase: ";
			    String docType = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " + "Transitional//EN\">\n";
			    out.println(docType +
			                "<HTML>\n" +
			                "<HEAD><TITLE>" + title + "</TITLE><link rel=\"stylesheet\" type=\"text/css\" href=\"page.css\" /></HEAD>\n" +
			    			"	<BODY id=\"main\">" +							
			    			"		<div class=\"body\">" +
			    			"			<div class=\"head shadow\">" +
							"				<div class=\"navBar\">" +
							"					<ul>" +
							"						<li class=\"side-by-side\"><a href=/MovieDB/Home>Home</a></li>" +
							"						<li class=\"side-by-side\"><a href=/MovieDB/SignOut>Sign out</a></li>" +							
							"					</ul>" +
							"				</div>"	+
							"				<div class=\"logo\">" +
							"					<h1>Fablix</h1>" +
							"				</div>" +							
							"			</div>" +
							"			<div class=\"mainContent\">" +
							"				<H1>" + title + "</H1>");		   
			    synchronized(movieItems)
			    {	
			    	if (movieItems.size() == 0) 				    	{
			    			
			    		out.println("<I>There is no items in your cart.</I>");
			    	} 
			    	else
			    	{
			    		//out.println("<p align=\"right\"> <p> <a href=\"/MovieDB/Process.jsp\"> Check Out</a></p>");
			    		out.println("<div class=\"cartThumb marginLeft\"><ul>");
			    		for(int i = 1; i<movieItems.size(); i = i + 4)
			    		{
			    			out.println("<LI>" +
			    					"		<div>" +
			    					"			<ul>" +
			    					"				<li class =\"side-by-side\"><img onerror=\"this.onerror=null;this.src='http://images1.wikia.nocookie.net/__cb20110525004732/gleeusers/images/a/af/Image_not_found.jpg'\" src="  + (String)movieItems.get(i) + "></li>" +
			    					"				<li class =\"side-by-side\"><a href=/MovieDB/Movie?id=" + (String)movieItems.get(i - 1) + ">" + (String)movieItems.get(i + 1) + "</a></li>" +
			    					"				<li class =\"side-by-side\"><b>Quantity: </b>" + (String)movieItems.get(i + 2) + "</li>" +
			    					"			</ul>" +
			    					"		</div>" +
			    					"	</li>");
			    		}		    
			        out.println("</UL></div>" +
			        				"<div align=\"center\" style=\"margin-top:20px;\"><h1><b>Your purchase transaction has been processed.</div>");
			    	}
			    } 
			    out.println("			</div>" +
		    			"		</div>" +
		    			"	</Body>" +
		    			"</html>"); 
		 	}
		 else {
				out.println("<HTML>" +
						"	<HEAD>" +
						"		<TITLE>Fablix movie database</TITLE>" +							
						"		<link rel=\"stylesheet\" type=\"text/css\" href=\"page.css\" />" +
						"	</HEAD>" +
						"	<BODY id=\"main\">" +							
						"		<div class=\"body\">" +
						"			<div class=\"head shadow\">" +					
						"				<div class=\"logo\">" +
						"					<h1>Fablix</h1>" +
						"				</div>" +							
						"			</div>" +
						"			<div class=\"mainContent\">" +
						"				<center><b>You are not allowed to view this page. Please try to <a href=/MovieDB/index.html>log in.</a></b><center>" +
						"			</div>" +
						"		</div>" +	
						"	</BODY>" +
						"</HTML>"); 
		 	}
		 session.setAttribute("movieItems", null);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
