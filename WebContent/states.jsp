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
	try {
		String connectionURL = "jdbc:mysql://localhost:3306/moviedb";
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		// Get a Connection to the database
		con = DriverManager.getConnection(connectionURL, "testuser", "testpass"); 
		//Add the data into the database
		String tp = str;
		if(!tp.trim().equalsIgnoreCase(""))
		{
			String [] temp;
			temp = str.split("\\s+");		
			String query = "Select id, title from movies where match(title) against ('";
			int size = temp.length;
			for(int i = 0; i < size - 1; ++i){
				query += "+" + temp[i] + " "; 
			}
			query += temp[temp.length - 1] + "*";
			query += "' in boolean mode) limit 10";
	
			Statement stm = con.createStatement();
			stm.executeQuery(query);
			ResultSet rs= stm.getResultSet();
			out.println("<div id=\"title\"><ul>");
			while (rs.next ()){
				out.println("<li><a href=/MovieDB/Movie?id=" + rs.getString("id") + ">" + rs.getString("title") + "</li>");
			}
			out.println("</ul></div>");
			stm.close();
		}
		con.close();
		}catch(Exception e){
		out.println("Exception is ;"+ e);			
	}
%>
</body>
</html>