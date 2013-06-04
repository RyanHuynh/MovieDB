

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Servlet implementation class MovieDB
 */
public class MovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	String genres_Query = null;
    String stars_Query = null;	    
    String keywords; 
    String searchType;
    String browse; 
    String pageSize; 
    String sortType;
    String sortQuery;
    int page = 1;
    int totalResult;
    Connection dbcon;        
    int pgSize = 20;
    HashMap<String, Integer> optionsMap = new HashMap<String, Integer>();
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieList() {    	
        super();
        optionsMap.put("10", 0);
        optionsMap.put("20", 1);
        optionsMap.put("30", 2);
        optionsMap.put("50", 3);
        optionsMap.put("title(asc)", 0);
        optionsMap.put("title(desc)", 1);
        optionsMap.put("year(asc)", 2);
        optionsMap.put("year(desc)", 3);
        						
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		getConnection();
		
		//Read query
		keywords = request.getParameter("keywords");
		searchType = request.getParameter("by");
	    browse = request.getParameter("browse");
	    pageSize = request.getParameter("pageSize");
	    sortType = request.getParameter("sort");
	    
	    if(request.getParameter("pg") != null)
	    	page = Integer.parseInt(request.getParameter("pg"));
	    else
	    	page = 1;	    
	    ArrayList<String> options = new ArrayList<String>();
	    options.add("");
	    options.add("");
	    options.add("");
	    options.add("");
	    ArrayList<String> sortOptions = new ArrayList<String>();
	    sortOptions.add("");
	    sortOptions.add("");
	    sortOptions.add("");
	    sortOptions.add("");
	    if(pageSize == null)
	    {
	    	options.set(1, "selected");
	    	pgSize = 20;
	    }
	    else
	    	pgSize = Integer.parseInt(pageSize);
	    if(sortType == null)
	    {
	    	sortType = "title(asc)";
	    	sortQuery= "r.title ASC";
	    	sortOptions.set(0, "selected");
	    }
	    else
	    {
	    	if(sortType.equalsIgnoreCase("title(asc)"))
	    			sortQuery = "r.title ASC";
	    	else if(sortType.equalsIgnoreCase("title(desc)"))
	    			sortQuery = "r.title DESC";
	    	else if(sortType.equalsIgnoreCase("year(asc)"))
	    			sortQuery = "r.year ASC";
	    	else
	    		sortQuery = "r.year DESC";
	    		
	    }
	    	
	    // Output stream to STDOUT
	    response.setContentType("text/html");		
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(true);
		Object loggedIn = session.getAttribute("LoginInPage");
        if (loggedIn != null && (Integer) loggedIn == 1)
		{			
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
	    			"					<div class =\"displayType\">" +
	    			"						<div  class=\"column\"> "+sortTable(sortOptions, readStringQuery()) + "</div>" +
	    			"              			<div  class=\"column right\"> "+ pagination(options, readStringQuery()) + "</div>" +	    			
	    			"					</div>" +
	    			"					<div class=\"padding\">" +  	   
										displayMainResult() +		
					"					</div>" +
	    			"					<div class=\"pageChange\">" +
	    								changePage(readStringQuery()) +
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
	private ResultSet getMovie() throws SQLException
	{
		ResultSet result = null;
		String query = null;
		Statement stament = dbcon.createStatement();
		if(browse != null)
	        { 
	        	if(browse.equalsIgnoreCase("0-9"))
	        	{
	        		query = "SELECT DISTINCT m.*" +
	        				  " FROM movies m INNER JOIN genres_in_movies gim " +
	        				  " ON gim.movie_id = m.id INNER JOIN genres g" +
	        				  " ON g.id = gim.genre_id" +
	        				  " WHERE m.title like '0%'" +
	        				  " OR m.title like '1%'" +
	        				  " OR m.title like '2%'" +
	        				  " OR m.title like '3%'" +
	        				  " OR m.title like '4%'" +
	        				  " OR m.title like '5%'" +
	        				  " OR m.title like '6%'" +
	        				  " OR m.title like '7%'" +
	        				  " OR m.title like '9%'";
	        	}
	        	else	        	
	        	{
	        		query = "SELECT DISTINCT m.*" +
	        				  " FROM movies m INNER JOIN genres_in_movies gim " +
	        				  " ON gim.movie_id = m.id INNER JOIN genres g" +
	        				  " ON g.id = gim.genre_id" +
	        				  " WHERE g.name ='" + browse + "'" +
	        				  " OR m.title like '" + browse + "%'";
	        	}
	        }
	        else
	        {
	        	if(searchType.equalsIgnoreCase("all"))
		        	{
		        	Pattern p = Pattern.compile("(\\w+)");
		        	Matcher m = p.matcher(keywords);
		        	String key = "";	
		        	if(m.find())
		        	{
		        		key = m.group(1);
		        		query = "SELECT DISTINCT m.*" +
								" FROM stars s INNER JOIN stars_in_movies sim" +
								" ON s.id = sim.star_id INNER JOIN movies m" +
								" ON m.id = sim.movie_id" +
								" WHERE m.title like '%" + key + "%'" +
										" OR m.year like '%" + key + "%'" +
										" OR s.first_name like '%" + key + "%'" +
										" OR s.last_name like '%" + key + "%'" +
										" OR m.director like '%" + key + "%'";
		        	}	        	
		        	while(m.find())
		        	{	        	
		        		key = m.group(1);
		        		query += " UNION" +
		        				" (SELECT DISTINCT m.*" +
								" FROM stars s INNER JOIN stars_in_movies sim" +
								" ON s.id = sim.star_id INNER JOIN movies m" +
								" ON m.id = sim.movie_id" +
								" WHERE m.title like '%" + key + "%'" +
										" OR m.year like '%" + key + "%'" +
										" OR s.first_name like '%" + key + "%'" +
										" OR s.last_name like '%" + key + "%'" +
										" OR m.director like '%" + key + "%' )";	        		
		        		
		        	}	        	
		        }
		        else  if(searchType.equalsIgnoreCase("first_name"))
		        {
		        	query = "SELECT m.*" +
							"FROM (SELECT stars_in_movies.movie_id " +
					   		 "FROM stars INNER JOIN stars_in_movies ON stars.id = stars_in_movies.star_id " +
					         "WHERE first_name like '%" + keywords +"%') as t INNER JOIN movies m" +
					         		" ON m.id = t.movie_id";	        	
		        }
		        else if(searchType.equalsIgnoreCase("last_name"))
		        {
		        	query = "SELECT m.*" +
							"FROM (SELECT stars_in_movies.movie_id " +
						   		 "FROM stars INNER JOIN stars_in_movies ON stars.id = stars_in_movies.star_id " +
						         "WHERE last_name like '%" + keywords +"%') as t INNER JOIN movies m" +
						         		" ON m.id = t.movie_id";		        	
		        }
		        else  if(searchType.equalsIgnoreCase("name"))
		        {
		        	Pattern p = Pattern.compile("(\\w*)\\s(\\w*)");
		        	Matcher m = p.matcher(keywords);
		        	String firstName = "";
		        	String lastName= "";
		        	if(m.find())
		        	{
		        		firstName = m.group(1);
		        		lastName = m.group(2);
		        	}
		        	//out.println("<H1 align=\"Center\">" + m.groupCount() + "</H1>");
		        	query = "SELECT m.*" +
							"FROM (SELECT stars_in_movies.movie_id " +
						   		 "FROM stars INNER JOIN stars_in_movies ON stars.id = stars_in_movies.star_id " +
						         "WHERE last_name like '%" +  lastName +"%' and first_name like '%" + firstName +"%') as t INNER JOIN movies m" +
						         		" ON m.id = t.movie_id";	        	
		        }
		        else
		        {
		        	query = "SELECT * from movies where " + searchType + " like \"%" + keywords + "%\"";	        	
		        }
	        }
		result = stament.executeQuery(query);
		if(result.last())		
			totalResult = result.getRow();
		else
			totalResult = 0;
		
		query = "SELECT t.* FROM (SELECT * FROM (" + query + ") as r ORDER BY " + sortQuery + " ) as t LIMIT " + pgSize + " OFFSET " + ((page - 1)* pgSize);
		result = stament.executeQuery(query);			
		return result;
	}
	private String pagination(ArrayList<String> options, String queryOutput)
	{					
		String result = "";   
		String choice = "" + pgSize + "";
	    //Numbers result per page 		
	    options.set(optionsMap.get(choice), "selected");                
	    result += "<form action=/MovieDB/MovieList method=get>" +
	    		  "Display <select id=\"pageSize\" name=\"pageSize\" onchange =\"changeDisplay(this,'/MovieDB/MovieList?"+ queryOutput + "');\">" +
	    		  "<option value=\"10\"" + options.get(0) +">10</option>" +
	    		  "<option value=\"20\"" + options.get(1) +">20</option>" +
	    		  "<option value=\"30\"" + options.get(2) +">30</option>" +
	    		  "<option value=\"50\"" + options.get(3) +">50</option>" +
	    		  "</select> per page.</form>";        		       
        return result;        
	}
	private String displayMainResult()
	{
		String result = "";
		 // Declare our statement	
		try
		{
	        Statement statement = dbcon.createStatement();	     
	        ResultSet movie = null;	         
	        //Generate Query	       
	        movie = getMovie();
	        if(!movie.next())
	        	result += "<h1>There is no movies matching your search.</h1>";
	        else
	        {
				//Out put result
		        result += "<TABLE id=\"movieList\" cellspacing=\"2\" style=\"width: 1000px;\" border>" + 
		    			  "<tr><th>ID</th>" + 
		    			  "<th>Title</th>" +
		    			  "<th>Year</th>" +
		    			  "<th>Director</th>" +
		    			  "<th>Genres</th>" +
		    			  "<th>Stars</th></tr>";	        
		        do 
		        { 
		            String id = movie.getString("ID");
		            String title = movie.getString("title");
		            String year = movie.getString("year");
		            String director = movie.getString("director");
		            result +=	"<tr>" +
		                        "<td>" + id + "</td>" +
		                        "<td><a href=/MovieDB/Movie?id=" + id + ">" + title + "</a></td>" +
		                        "<td>" + year + "</td>" +
		                        "<td>" + director + "</td>";
		            //get genres of this movie
		            genres_Query = "SELECT g.name " +
								" FROM genres_in_movies gim JOIN movies m" +
								" ON m.id = gim.movie_id JOIN genres g" +
								" ON g.id = gim.genre_id" +
								" WHERE m.id = " + id;
		            
		            //get stars of this movie
		            stars_Query = "SELECT s.first_name, s.last_name, s.id " +
								  " FROM stars_in_movies sim JOIN stars s" +
								  " ON s.id = sim.star_id JOIN movies m" +
								  " ON m.id = sim.movie_id" +
								  " WHERE m.id = " + id;
	            
		            ResultSet genres = statement.executeQuery(genres_Query);
		            result += "<td>";
		            if(genres.next())
		            	result += genres.getString(1);
			        while(genres.next())
			        	result +=  ", " + genres.getString(1);
			        result += ".</td>";
			        
			        ResultSet stars = statement.executeQuery(stars_Query);
			        result += "<td>";
		            if(stars.next())
		            {     
			        	String star = stars.getString(1) + " " + stars.getString(2); 
			        	result += "<a href=/MovieDB/Star?id=" + stars.getString("id") + ">" + star + "</a>";
		            }  
		            while(stars.next())
		            {     
		            	String star = stars.getString(1) + " " + stars.getString(2); 
		            	result += ", <a href=/MovieDB/Star?id=" + stars.getString("id") + ">" + star + "</a>";
		            }    	        
		            result += ".</td></tr>";    	       
		        }
		        while(movie.next());
		        result += "</table>";  
	        }
	        statement.close();
		}
		catch (SQLException e) {			
			e.printStackTrace();
		}
        return result;
	}
	private String changePage(String queryOutput)
	{
		String result = "";
		if(totalResult != 0)
		{
			result = "<ul>" + 
							"	<li class=\"side-by-side\">";
			if(page > 1)       
	        	result += 	"	<a href=/MovieDB/MovieList?" +  queryOutput + "&pg=" + (page - 1) +">Prev</a>";
	        int maxPage = totalResult/pgSize;
	        if(totalResult%pgSize != 0)
	        	maxPage++;
	        result += 		"	</li>" + 
	        				"	<li class=\"side-by-side\">" + page + " of " + maxPage + "</li>" +
	        				"	<li class=\"side-by-side\">";         					
	        if(page < maxPage)        
	        	result += 	"	<a href=/MovieDB/MovieList?" +  queryOutput + "&pg=" + (page + 1) +">Next</a>";
	        result += 		"	</li>" +
	        				"</ul>";
		}
		return result;
	}
	private String readStringQuery()
	{		
		 String result = "";
		 if(searchType != null)
	         	result += "by=" + searchType + "&keywords=" + keywords;
	         else
	         	result += "browse=" + browse;		
		 if(pageSize != null)
			 result += "&pageSize=" + pageSize;
		 if(sortType != null)
			 result += "&sort=" + sortType;
		 return result;
	}
	private String sortTable(ArrayList<String> sortOptions, String queryOutput)
	{
		String result = "";   
		String choice = sortType;
	    //Numbers result per page 		
	    sortOptions.set(optionsMap.get(choice), "selected");                
	    result += "<form action=/MovieDB/MovieList method=get>" +
	    		  "Sort by <select id=\"sortTable\" name=\"sortTable\" onchange =\"tableSort(this,'/MovieDB/MovieList?"+ queryOutput + "');\">" +
	    		  "<option value=\"title(asc)\"" + sortOptions.get(0) +">Title(Ascending)</option>" +
	    		  "<option value=\"title(desc)\"" + sortOptions.get(1) +">Title(Descending)</option>" +
	    		  "<option value=\"year(asc)\"" + sortOptions.get(2) +">Year(Ascending)</option>" +
	    		  "<option value=\"year(desc)\"" + sortOptions.get(3) +">Year(Descending)</option>" +
	    		  "</select></form>";        		       
        return result;        
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
				  "function openPopup(inputString) " +
				  "{" +	
				  "		$.post(\"/MovieDB/popup.jsp\", " +
				  "			{" +
				  "				queryString: \"\" + inputString + \"\"" +
				  "			}," +
				  " 		function(data)" +
				  "			{" +			
				  "				$('#' + inputString + ' ' + '.details').show();" +
				  "				$('#' + inputString + ' ' + '.details').html(data);" +
				  "		});" +
				  "}" + 	
				  
				  "function closePopup(inputString) {" +	
				  "		$('.details').hide();}" +
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
