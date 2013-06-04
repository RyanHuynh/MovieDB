<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% response.setContentType("text/html");%>
<%
	String str = request.getParameter("queryString");
	String result = "";
	try {
		String connectionURL = "jdbc:mysql://localhost:3306/moviedb";
		Connection dbcon;
		Class.forName("com.mysql.jdbc.Driver");
		// Get a Connection to the database
		dbcon = DriverManager.getConnection(connectionURL, "testuser", "testpass"); 
		//Add the data into the database
		
		 Statement statement1 = dbcon.createStatement();	  
	     Statement statement2 = dbcon.createStatement();
	     String query = "SELECT *" + 
	        			" FROM movies" +
	        			" WHERE id = " + str;
        ResultSet rs = statement1.executeQuery(query);
        while (rs.next())
        {           
            String title = rs.getString("title");
            String year = rs.getString("year");
            String director = rs.getString("director");           
            result += 	"	<ul>" +	 
                        "		<li><b>Title:</b> " + title + "</li>" +                      
                        "		<li><b>Year:</b> " + year + "</li>" +
                        "		<li><b>Director:</b> " +  director + "</li>";
	            
	            String genres_Query = "SELECT g.name " +
						" FROM genres_in_movies gim JOIN genres g" +
						" ON g.id = gim.genre_id" +						
						" WHERE gim.movie_id = " + str;          	
	            ResultSet genres = statement2.executeQuery(genres_Query);
	            result += "<li><b>Genres: </b> ";
	            if(genres.next())	        	    	        	
		        	result += genres.getString("name");		        
		        while(genres.next())
		        {     		        	     	        	
		        	result += ", " + genres.getString("name");
		        }    	        
		        result += ".</li>";
		        
	            String stars_Query = "SELECT s.first_name, s.last_name, s.id " +
						" FROM stars_in_movies sim JOIN stars s" +
						" ON s.id = sim.star_id JOIN movies m" +
						" ON m.id = sim.movie_id" +
						" WHERE m.id = " + str;    
	            
	            ResultSet stars = statement2.executeQuery(stars_Query);
	            result += "<li><b>Stars:</b> ";
	            if(stars.next())
		        {     
		        	String star = stars.getString(1) + " " + stars.getString(2);     	        	
		        	result += star ;
		        }   
		        while(stars.next())
		        {     
		        	String star = stars.getString(1) + " " + stars.getString(2);     	        	
		        	result += ", " + star;
		        }
		        out.println(result);		       
        }
        statement2.close();
        statement1.close();
        dbcon.close();
		}catch(Exception e){
		out.println("Exception is ;"+ e);		
	}
%>
</body>
</html>