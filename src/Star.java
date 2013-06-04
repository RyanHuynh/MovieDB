

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Star
 */
public class Star extends HttpServlet {
	private static final long serialVersionUID = 1L;
     Connection dbcon;
     String id;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Star() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getConnection();
		id = request.getParameter("id");
		 // Output stream to STDOUT
	    response.setContentType("text/html");		
        PrintWriter out = response.getWriter();
        
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
					"			<div class=\"mainContent\">");
		HttpSession session = request.getSession();
		Object loggedIn = session.getAttribute("LoginInPage");
		if (loggedIn != null && (Integer) loggedIn == 1) {			
			
			out.print("				<div class=\"column shadow leftColumn\">" +
	    			"					<div class =\"searchBar\" >" +
	    								searchBar() +
	    			"					</div>" +	    								
	    			"					<div class=\"center\"  align=\"center\">" +  	   
										getStarInfo() +		
					"					</div>" +	    								
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
			out.println(
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
		// TODO Auto-generated method stub
	}
	private String getStarInfo()
	{
		String result = "";
		try
		{	
			Statement statement1 = dbcon.createStatement();	  
	        Statement statement2 = dbcon.createStatement();
	        String query = "SELECT *" + 
	        			   " FROM stars" +
	        			   " WHERE id = " + id;
	        ResultSet rs = statement1.executeQuery(query);
	        while (rs.next())
	        { 
	        	String starId = rs.getString("ID");
	            String dob = rs.getString("dob");
	            String photo = rs.getString("photo_url");
	            String name = rs.getString("first_name") + " " + rs.getString("last_name");
	            result += "<div class=\"mainImg column\">" +
	            		  "		<img onerror=\"this.onerror=null;this.src='http://images1.wikia.nocookie.net/__cb20110525004732/gleeusers/images/a/af/Image_not_found.jpg'\" src =" + photo + ">" +
	            		  "</div>" +
	            		  "<div class=\"info column\">" +
	            		  " 	<ul>" +	            		 
	            		  "			<li><b>ID:</b> " + starId + "</li>" +
	            		  "			<li><b>Name:</b> " + name + "</li>" +
	            		  "			<li><b>DOB:</b> " + dob + "</li>" +                           
                       	  "			<li><b>Known for:</b> </li>" +
                       	  "			<ul>";
	            query = "SELECT m.title, m.id" + 
	            		" FROM stars s INNER JOIN stars_in_movies sim" +
	            		" ON s.id = sim.star_id INNER JOIN movies m" +
	            		" ON m.id = sim.movie_id" +
	            		" WHERE s.id = " + id;
	            ResultSet movies = statement2.executeQuery(query);
	            while(movies.next())
	            {
	            	result += "<li><a href=/MovieDB/Movie?id=" + movies.getString("id") + ">" +  movies.getString("title") + "</a></li>";
	            }
	            result +="</ul></ul></div>";
	        }
		}
	    catch (SQLException e) {			
			e.printStackTrace();
		}
        return result;        
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
	private String searchBar()
	{
		String result = "";
		result += "<form action=\"/MovieDB/MovieList\" method=\"get\"  style=\"margin: 0pt;\">" + 
				   "	<INPUT TYPE=\"TEXT\" NAME=\"keywords\" style=\"width: 500px\" onkeyup=\"lookup(this.value);\" onblur=\"fill();\">" + 
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
				 "function fill(thisValue) {" +
				 "	$('#inputString').val(thisValue);" +
				 "		setTimeout(\"$('#suggestions').hide();\", 200);" +
				 "}" +
				 "</script>";		
		return result;
	}
}
