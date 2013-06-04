

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Servlet implementation class MovieDB
 */
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	
    int page = 1;
    int totalResult;
    Connection dbcon;     
    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {    	
        super();
       
        						
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		getConnection();
		
		// Output stream to STDOUT
	    response.setContentType("text/html");		
        PrintWriter out = response.getWriter();				
        
		 if(request.getParameter("pg") != null)
		    	page = Integer.parseInt(request.getParameter("pg"));
		    else
		    	page = 1;	   
        
        HttpSession session = request.getSession(true);
		Object loggedIn = session.getAttribute("LoginInPage");
		if (loggedIn != null && (Integer) loggedIn == 1)
		{		
			/*DataSource ds = (DataSource) session.getAttribute("Datasource");
			try {
				dbcon = ds.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//HTML
			out.println("<HTML>" +
					"	<HEAD>" +
					"		<TITLE>Fablix movie database</TITLE>" +
							javaScript() +	
					"		<link rel=\"stylesheet\" type=\"text/css\" href=\"page.css\" />" +
					"	</HEAD>" +
					"	<BODY id=\"main\">" +							
	    			"		<div class=\"body\">" +
	    			"			<div class=\"head shadow\">" +
					"				<div class=\"navBar\">" +
					"					<ul>" +
					"						<li class=\"side-by-side\"><a href=/MovieDB/Home>Home</a></li>" +
					"						<li class=\"side-by-side\"><a href=/MovieDB/SignOut>Sign out</a></li>" +
					"						<li class=\"side-by-side\"><a href=/MovieDB/ShoppingCart>View Cart</a></li>" +
					"					</ul>" +
					"				</div>"	+
					"				<div class=\"logo\">" +
					"					<h1>Fablix</h1>" +
					"				</div>" +							
					"			</div>" +
					"			<div class=\"mainContent\">" +
	    			"				<div class=\"column shadow leftColumn\">" +
	    			"					<div class =\"searchBar\" >" +
	    								searchBar() +
	    			"					</div>" +	    			
	    			"					<div id=\"main_content\" class=\"padding\">" +  	   
										getMovie() +		
					"					</div>" +
	    			"					<div class=\"pageChange\">" +
	    								changePage() +
	    			"					</div>"	+
	    			"				</div>" +
	    			"				<div class=\"column shadow padding rightColumn\">" +
	    			"					<div>"+
	    			"					<h1>Genres</h1>" +	
	    								browseGenres() +
	    			"					</div>"+	
	    			"					<div class=\"marginTop\">"+
	    			"					<h1>Title</h1>" +	
	    								browseTitles() +
	    			"					</div>"+	
	    			"				</div>" +
	    			"			</div>" +
	    			"		</div>"	+
	    			"	</BODY>" +
	    			"</HTML>"); 
		}			
		else
		{					
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
        out.close();
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
	private String getMovie() 
	{
		String result ="";		
		ResultSet rs = null;
		String query = null;
		try
		{
			Statement stament = dbcon.createStatement();
			query = "SELECT title, banner_url, id" +
					" FROM movies";
			rs = stament.executeQuery(query);
			if(rs.last())		
				totalResult = rs.getRow();
			
			query = "SELECT t.* FROM (" + query + ") as t LIMIT 25 OFFSET " + ((page - 1) * 25);			
			rs = stament.executeQuery(query);				
			int count = 5;
			result += "<ul>";
			while(rs.next())
			{
				if(count == 5)
					result += "<li>";
				count--;
				result += "<div id=" + rs.getString("id") + " class=\"column thumbMovie\">" +
						  "		<div class=\"column\">" +
						  "			<div class=\"thumbImg\">" +
						  "				<a onmouseover=\"openPopup('" + rs.getString("id") + "')\" onmouseout=\"closePopup(this)\" href=/MovieDB/Movie?id=" + rs.getString("id") + ">" +
						  "					<img onerror=\"this.onerror=null;this.src='http://images1.wikia.nocookie.net/__cb20110525004732/gleeusers/images/a/af/Image_not_found.jpg'\" src=" + rs.getString(2) + ">" +
						  "				</a>" +
						  "			</div>" + 
						  "			<div><b>" + rs.getString(1) + "</b></div>" +
						  "		</div>" +
						  "		<div class=\"details \" ></div>" +
						  "</div>";
				if(count == 0)
				{
					count = 5;
					result += "</li>";
				}
			}
			if(count != 5)
				result += "</li>";			
			result += "</ul>";
			stament.close();
		}
		catch (SQLException e) {			
			e.printStackTrace();
		}
		return result;
	}
	
	
	private String javaScript()
	{
		String result = "";	
		
		//sortTable function
		result += "<script defer=\"defer\" language=\"javascript\" type=\"text/javascript\">" +
				  "		function tableSort(el,url)" +
				  "		{" +					
				  "			if(url.indexOf(\"sort\") >=0)" +
				  "			{" +
				  "				var ind1 = url.indexOf(\"sort\");" +
				  "				var ind2 = url.indexOf(\"&\",ind1);" +
				  "				if(ind2 >=0)" +
				  "				{" +
				  "					var left = url.substring(0,ind1-1);" +
				  "					var right= url.substring(ind2,url.length);" +
				  "					url = left+right;" +
				  "				}" +
				  "				else{" +
				  "					url = url.substring(0,ind1-1);" +
				  "				}" +
				  "			}" +
				  "			if(url.indexOf(\"pg\") >=0)" +
				  "			{" +
				  "				var ind1 = url.indexOf(\"pg\");" +
				  "				var ind2 = url.indexOf(\"&\",ind1);" +
				  "				if(ind2 >=0)" +
				  "				{" +
				  "					var left = url.substring(0,ind1-1);" +
				  "					var right = url.substring(ind2,url.length);" +
				  "					url = left + right;" +
				  "				}" +
				  "				else{" +
				  "					url = url.substring(0,ind1-1);" +
				  "				}" +
				  "			}" +
				  "			var idx = el.selectedIndex;" +
				  "			var selValue = el.options[idx].value;" +
				  "			url += \"&pg=1&sort=\" + selValue;" +
				  "			if(url != null)" +
				  "			{" +
				  "				window.location.href = url;" +
				  "			}" +
				  "		}" +
				  "		</script>";
		
		//changeDisplay function
		result += "<script defer=\"defer\" language=\"javascript\" type=\"text/javascript\">" +
				  "		function changeDisplay(el,url)" +
				  "		{" +					
				  "			if(url.indexOf(\"pageSize\") >=0)" +
				  "			{" +
				  "				var ind1 = url.indexOf(\"pageSize\");" +
				  "				var ind2 = url.indexOf(\"&\",ind1);" +
				  "				if(ind2 >=0)" +
				  "				{" +
				  "					var left = url.substring(0,ind1-1);" +
				  "					var right= url.substring(ind2,url.length);" +
				  "					url = left+right;" +
				  "				}" +
				  "				else{" +
				  "					url = url.substring(0,ind1-1)" +
				  "				}" +
				  "			}" +
				  "			if(url.indexOf(\"pg\") >=0)" +
				  "			{" +
				  "				var ind1 = url.indexOf(\"pg\");" +
				  "				var ind2 = url.indexOf(\"&\",ind1);" +
				  "				if(ind2 >=0)" +
				  "				{" +
				  "					var left = url.substring(0,ind1-1);" +
				  "					var right = url.substring(ind2,url.length);" +
				  "					url = left + right;" +
				  "				}" +
				  "				else{" +
				  "					url = url.substring(0,ind1-1);" +
				  "				}" +
				  "			}" +
				  "			var idx = el.selectedIndex;" +
				  "			var selValue = el.options[idx].value;" +
				  "			url += \"&pg=1&pageSize=\" + selValue;" +
				  "			if(url != null)" +
				  "			{" +
				  "				window.location.href = url;" +
				  "			}" +
				  "		}" +
				  "		</script>";
		//Image Hover
		result += "<script defer=\"defer\" language=\"javascript\" type=\"text/javascript\">" +
				  "function openPopup(inputString) {" +	
				  "$.post(\"/MovieDB/popup.jsp\", {queryString: \"\" + inputString + \"\"}, function(data){" +			
				  "$('#' + inputString + ' ' + '.details').show();" +
				  "$('#' + inputString + ' ' + '.details').html(data);" +
				  "});}" + 	
				  "function closePopup(inputString) {" +	
				  "$('.details').hide();}" +
				  "</script>";
		
		//Suggestion box
		result +="<script type=\"text/javascript\" src=\"../Scripts/jquery-1.7.2.min.js\"></script>" +
				 "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"   +
				 "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js\"></script>" +
				 "<script type=\"text/javascript\">" + 
				 "function lookup(inputString) {"    +
				 "	if(inputString.length == 0) {"     +
				 "		$('#suggestions').hide();"     +
				 "	} else {" 						   +
				 "		$.post(\"/MovieDB/states.jsp\", {queryString: \"\" +inputString+ \"\"}, function(data){"    +
				 "			if(data.length >0) {" 	   +
				 "				$('#suggestions').show();" +
				 "					$('#autoSuggestionsList').html(data);" +
				 "			}" +
				 "		});" +
				 "	}" +
				 "}" +				
				 "</script>";		
		return result;
	}
	private String changePage()
	{
		String result = "<ul>" + 
						"	<li class=\"side-by-side\">";
		if(page > 1)       
        	result += 	"	<a href=/MovieDB/Home?" + "&pg=" + (page - 1) +">Prev</a>";
        int maxPage = totalResult/25;
        if(totalResult%25 != 0)
        	maxPage++;
        result += 		"	</li>" + 
        				"	<li class=\"side-by-side\">" + page + " of " + maxPage + "</li>" +
        				"	<li class=\"side-by-side\">";         					
        if(page < maxPage)        
        	result += 	"	<a href=/MovieDB/Home?" + "pg=" + (page + 1) +">Next</a>";
        result += 		"	</li>" +
        				"</ul>";
		return result;
	}

	private String searchBar()
	{
		String result = "";
		result += "<form action=\"/MovieDB/MovieList\" method=\"get\"  style=\"margin: 0pt;\">" + 
				   "	<INPUT TYPE=\"TEXT\" NAME=\"keywords\" style=\"width: 500px\" onkeyup=\"lookup(this.value);\">" + 
				   "	 	<select name=\"by\" style=\"width: 100px\">" + 
				   "			<option value=\"all\">All</option>" + 
				   "			<option value=\"title\">Title</option>" +
				   "			<option value=\"year\">Year</option>" +
				   "			<option value=\"director\">Director</option>" +
				   "			<option value=\"first_name\">First Name</option>" +
				   "			<option value=\"last_name\">Last Name</option>" +
				   "			<option value=\"name\">Name</option>" +	
				   "		</select>" +
				   "	<INPUT TYPE=\"SUBMIT\" VALUE=\"Search\">" +
				   "	<div class=\"suggestionsBox \" id=\"suggestions\" style=\"display: none;\">" +
				   "		<div class=\"suggestionList\" id=\"autoSuggestionsList\"></div>" +
				   "	</div>" + 
				   "</form>";
		return result;
	}	 	
	
	private String browseTitles()
	{
		String str ="ABCDEFGHIJKLMNOPQRSTUVWYZ";
    	String result = "<table class=\"browse\">";
    	for(int i = 0; i < str.length(); ++i)
    	{
    		if(i % 10 == 0)
    		{
    			result += "<tr>";
    		}
    		result += "<td><a href=/MovieDB/MovieList?browse=" + str.charAt(i) + ">"+ str.charAt(i) + "</a></td>";
    		if((i % 10) + 1== 0)
    			result += "</tr>";
    	}
    	result += "<td><a href=/MovieDB/MovieList?browse=0-9>0-9</a></td>";
    	return result;
	}
	private String browseGenres()
	{
		String result = "";
		String query = "SELECT genres.name" + 
			   " FROM genres";	
		try{
		Statement stm = dbcon.createStatement();
		ResultSet rs = stm.executeQuery(query);
		result += "<table  class=\"browse\">";
		int count = 2;
		while(rs.next())
		{
			if(count == 2)
				result += "<tr>";
			count--;
			result +="<td><a href=/MovieDB/MovieList?browse=" + rs.getString("name")+">"+ rs.getString("name") + "</a></td>";
			if(count == 0)
			{
				count = 2;
				result += "</tr>";
			}
		}
		if(count != 2)
			result +="</tr>";
		result +="</table>";
		}
		catch (SQLException e) {			
			e.printStackTrace();
		}
		return result;
	}
}
