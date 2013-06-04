
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class LogInPage
 */

public class LogInPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       Connection dbcon;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogInPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		getConnection();
	     
        // Output stream to STDOUT
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try{        	
 	       	Statement statement = dbcon.createStatement();

  		   String email1 = request.getParameter("email");
           String password = request.getParameter("password");
           String query = "SELECT * from customers where email = '" + email1 + "'" + " AND password = '" + password + "'";
  		  
            // Perform the query
            ResultSet result = statement.executeQuery(query);
  		 
            //Initialize session.
            HttpSession session = request.getSession(true);
            synchronized(session){
  			session.setAttribute("LoginInPage", new Integer(0));
  		    }
            
            if(result.next()){          	  
          	  session.setAttribute("LoginInPage", new Integer(1));          	  
          	  session.setAttribute("UserName", result.getString("first_name"));          	 
          	  RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Home");
          	  dispatcher.include(request, response);	   
            }
            else{
            	out.print("<html>" +
            				"<head>" +            		
            					"<title>Fablix Movie Database</title>" +
            					"<link rel=\"stylesheet\" type=\"text/css\" href=\"page.css\"/>" +
            					"</head>" +
            					"<body id=\"main\">" +
            						"<div class=\"body\">" +
            						"<div class=\"head shadow\">" + 
            						"<div class=\"logo\">" + 
            							"<h1>Fablix</h1>" +
            						"</div> " +
            						"</div>" +
            						"<div class=\"mainContent\">" +
            							"<FORM ACTION=\"/MovieDB/LogInPage\" METHOD=\"post\">" +
            							"<div align=center style=\"margin-top:20px;\">" +
            								"<table style=\"text-align:left\">" + 
            								"<tr>" +
            									"<td>Email Address:</td>" +
            									"<td><INPUT TYPE=\"TEXT\" NAME=\"email\"></td>" +
            								"</tr>" +
            								"<tr>" +
            									"<td>Password:</td>" +
            									"<td><INPUT TYPE=\"PASSWORD\" NAME=\"password\"></td>" +
            								"</tr>" +
            								"</table>" +
            								"<INPUT style =\"margin-top:8px;\" TYPE=\"SUBMIT\" VALUE=\"Sign In\">" +
            								"<p><b>Invalid user name or password. Please enter again.</b></p>" +
            							"</div>" +
            							"</FORM>" +            							
            						"</div>" +
            					"</div>" +
            					"</body>" +
            				"</html>");
            }
            
            }catch (SQLException ex) {
                ex.printStackTrace();
        	    while (ex != null) {
                            System.out.println ("SQL Exception:  " + ex.getMessage ());
                            ex = ex.getNextException ();
                        }  
              }      
        
  	}
  	/**
  	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
  	 */
  	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		doGet(request, response);
  		  	}
  	private void getConnection() 
	{
		try
		{
			String loginUser = "testuser1";
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
