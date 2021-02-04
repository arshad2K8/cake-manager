<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.waracle.cakemgr.CakeEntity"%>
<%@ page import="java.util.List"%>
<%@ page import="com.waracle.cakemgr.service.CakeService"%>
<%@ page import="com.waracle.cakemgr.service.CakeServiceImpl"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<body>
<h2>Welcome to Cake Manager</h2>
	<%!
	    CakeService cakeService = new CakeServiceImpl();
		List<CakeEntity> cakes = cakeService.findAll();
	%>

	<table border="1" cellpadding="5">
		<thead>
            <tr>
                <td><strong>Title</strong></td>
                <td><strong>Description</strong></td>
                <td><strong>Image</strong></td>
            </tr>
		</thead>

        <tbody>
            <c:forEach items="<%= cakes %>" var="cake">
                <tr>
                    <td>${cake.title}</td>
                    <td>${cake.description}</td>
                    <td><img alt="Image" src="${cake.image}" height="50" width="50"/></td>
                </tr>
            </c:forEach>

        </tbody>
	</table>
</body>
</html>
