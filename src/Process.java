import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Process
 */

public class Process extends HttpServlet {
	private static final long serialVersionUID = 1L;
       Connection dbcon;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Process() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getConnection();
		
        PrintWriter out = response.getWriter();
		response.setContentType("text/html"); 
		ResultSet result = null;
		Statement statement = null;		
		String query = null;
		
		out.println("<HTML>" +
				"	<HEAD>" +
				"		<TITLE>Fablix movie database</TITLE>" +						
				"		<link rel=\"stylesheet\" type=\"text/css\" href=\"page.css\" />" +
				"	</HEAD>" +
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
				"			<div class=\"mainContent\">");
		
		HttpSession session = request.getSession();
		Object loggedIn = session.getAttribute("LoginInPage");
		if (loggedIn != null && (Integer) loggedIn == 1) {
		    String first_name = request.getParameter("first_name");
		    String last_name = request.getParameter("last_name");
		    String creditcard = request.getParameter("creditcard");
		    int cc = 0;
		    String expiration_date = request.getParameter("expiration_date");
		    if(first_name.equalsIgnoreCase("") || last_name.equalsIgnoreCase("") || creditcard.equalsIgnoreCase("") || creditcard.equalsIgnoreCase(""))		    	
		    {
		    	out.print("<center><b>Please make sure all the info are correct. Please try it <a href=/MovieDB/Process.jsp>again</a></b><center>");
		    	
		    }
		    else
		    {	   
		    	cc = Integer.parseInt(creditcard);
			    query = "Select * from creditcards " +
			    		" where first_name = '" + first_name + 
			    		"' AND last_name = '" + last_name +
			    		"' AND id = " + cc +
			    		" AND expiration = '" + expiration_date + "'";
			    try{				
			        statement  = dbcon.createStatement();
			        result = statement.executeQuery(query);
			        if(result.next()){
			        	response.sendRedirect("/MovieDB/ConfirmPage");
			        }
			        else{
			        	out.print("<center><b>Please make sure all the info are correct. Please try it <a href=/MovieDB/Process.jsp>again</a></b></center>");
			        }
		        
			    }catch (Exception e){
			    	out.println(e);
			    }	
		    }
		}
		else
		{
			out.println(					
					"				<center><b>You are not allowed to view this page. Please try to <a href=/MovieDB/Index.html>log in.</a></b><center>" +
	    			"			</div>" +
					"		</div>" +	
	    			"	</BODY>" +
	    			"</HTML>"); 
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	private void getConnection() 
	{
		try
		{
			String loginUser = "testuser";
		    String loginPasswd = "testpass";
		    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";    
		    //Class.forName("org.gjt.mm.mysql.Driver");
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	    	dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
		}
		catch (SQLException ex)
		{
            while (ex != null)
            {
                  System.out.println ("SQL Exception:  " + ex.getMessage ());
                  ex = ex.getNextException ();
            } 
		}
	  catch(java.lang.Exception ex)
      {          
          return;
      }
	}
}
