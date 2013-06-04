import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddToCart */

public class AddToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToCart() {
    	
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");
		 String updateID = "";
		 String updateQuantity;
	    PrintWriter out = response.getWriter();
	    HttpSession session = request.getSession();
		Object loggedIn = session.getAttribute("LoginInPage");
		if (loggedIn != null && (Integer) loggedIn == 1)
		{
			updateID = request.getParameter("id");
		    updateQuantity = request.getParameter("quantity");
	        String movieID = (String) session.getAttribute("MovieID");
	        String movieThumb = (String) session.getAttribute("MovieThumb");
	        String movieTitle = (String) session.getAttribute("MovieTitle");
	        ArrayList <String> movieItems = null;
		    synchronized(session)
		    {
		        movieItems = (ArrayList)session.getAttribute("movieItems");
		        if (movieItems == null)
		        {
		                movieItems = new ArrayList<String>();
		                session.setAttribute("movieItems", movieItems);
		        }
		    }
		    boolean check = false;
		    if(updateID == null)
		    {
		    	for(int i = 0; i < movieItems.size(); i = i + 4)
		    	{
		        	String id = (String)movieItems.get(i);
		        	if(id.equals(movieID))
		        	{	        		
		        		int quantity = Integer.parseInt(movieItems.get(i + 3)) + 1;	        		
		        		movieItems.set((i + 3), Integer.toString(quantity));
		        		check = true;
		        		break;
		        	}	        	
		    	}		    
			    if(check == false)
			    {
			    	movieItems.add(movieID);
			    	movieItems.add(movieThumb);
			    	movieItems.add(movieTitle);
			    	movieItems.add("1");
			    }	
		    }
		    else
		    {
		    	
		    	if(updateQuantity != null && movieItems.indexOf(updateID) != -1)
		    		if(updateQuantity.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")){	
		    			if(Integer.parseInt(updateQuantity) <= 0)
		    			{	    	
		    				int temp = movieItems.indexOf(updateID);
		    				movieItems.remove(temp);
		    				movieItems.remove(temp);
		    				movieItems.remove(temp);
		    				movieItems.remove(temp);
		    			}
				    	for(int i=0; i<movieItems.size(); i = i + 4)
				    	{
				        	String id = (String)movieItems.get(i);
					    	if(updateID.equals(id))
					    	{
					    		movieItems.set(i + 3, updateQuantity);					    	
					    		check = true;
					    		session.setAttribute("MovieID", null);
					    		break;
					    	}  
				    	}
		    		}
		    }	    
	   
		    String title = "Items purchase: ";
		    String docType = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " + "Transitional//EN\">\n";
		    out.println(docType +
		                "<HTML>\n" +
		                "<HEAD><TITLE>" + title + "</TITLE><link rel=\"stylesheet\" type=\"text/css\" href=\"page.css\" /></HEAD>\n" +
		                "<BODY BGCOLOR=\"#EFF8FB\">\n" +
		                "<H1>" + title + "</H1>" +
		    			"<p> <FORM ACTION=\"/MovieDB/AddToCart\"" +
		    			"METHOD=\"GET\">" +
    						"<CENTER>" +
    						"ID: <INPUT TYPE=\"TEXT\" NAME=\"id\">" +
    						"Quantity: <INPUT TYPE=\"TEXT\" NAME=\"quantity\">" +
    						"<INPUT TYPE=\"SUBMIT\" VALUE=\"Update\">" +
    						"</CENTER></FORM></p>");	
			out.println();
		    synchronized(movieItems) {			    	
		    		out.println("<UL class=\"cartThumb\">");
		    		for(int i = 1; i<movieItems.size(); i = i + 4)
		    		{
		    			out.println("<LI>" +
		    					"		<div>" +
		    					"			<ul>" +
		    					"				<li class =\"side-by-side\"><img onerror=\"this.onerror=null;this.src='http://images1.wikia.nocookie.net/__cb20110525004732/gleeusers/images/a/af/Image_not_found.jpg'\" src="  + (String)movieItems.get(i) + "></li>" +
		    					"				<li class =\"side-by-side\">(" + (String)movieItems.get(i - 1) + ")<a href=/MovieDB/Movie?id=" + (String)movieItems.get(i - 1) + ">" + (String)movieItems.get(i + 1) + "</a></li>" +
		    					"				<li class =\"side-by-side\"><b>Quantity: </b>" + (String)movieItems.get(i + 2) + "</li>" +
		    					"			</ul>" +
		    					"		</div>" +
		    					"	</li>");
		    		}		    
		        out.println("</UL><p align=\"right\"> <input type=\"button\" value=\"Close Window\" onclick=\"window.close()\"></p>");		      
		    }
		    out.println("</Body>"); 
		}
		else {
			out.println("Please " + "<a href=/MovieDB/index.html>"
					+ "log in </a>" + "to use Fablix");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
