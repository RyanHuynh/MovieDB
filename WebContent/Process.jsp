<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fablix Movie database</title>
<link rel="stylesheet" type="text/css" href="page.css"/>
</head>
<body id="main">
	<div class="body">
		<div class="head shadow">
			<div class="navBar">
				<ul>
					<li class="side-by-side"><a href=/MovieDB/Home>Home</a></li>
					<li class="side-by-side"><a href=/MovieDB/SignOut>Sign out</a></li>
				</ul>
			</div>
			<div class="logo">
				<h1>Fablix</h1>
			</div>
		</div>
		<div class="mainContent">	
<%
		Object loggedIn = session.getAttribute("LoginInPage");
		if (loggedIn != null && (Integer) loggedIn == 1) {
%>
			<H1 ALIGN="CENTER">Please fill out the info below. </H1>
			<FORM ACTION="/MovieDB/Process" METHOD="Get">
 			<div align=center style="margin-top:20px;">
 				<table style="text-align:left">
  					<tr>
  						<td>First Name: </td>
  						<td><INPUT TYPE="TEXT" NAME="first_name"></td>
  					</tr>
    				<tr>
    					<td>Last Name: </td>
    					<td><INPUT TYPE="Text" NAME="last_name"></td>
    				</tr>
    				<tr>
    					<td>Credit card: </td>
    					<td><INPUT TYPE="Password" NAME="creditcard"></td>
    				</tr>
    				<tr>
    					<td>Expiration Date(yyyy/mm/dd): </td>
    					<td><INPUT TYPE="Text" NAME="expiration_date"></td>
    				</tr>
    			</table>
    			<INPUT style ="margin-top:8px;" TYPE="SUBMIT" VALUE="Sign In">
    		</div>
  			</FORM>
<%
		}
		else {					
					out.print("<center><b>You are not allowed to view this page. Please try to <a href=/MovieDB/Index.html>log in.</a></b><center>");	    			
		}
%>
		</div>
	</div>
</body>
</html>