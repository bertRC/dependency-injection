<%@ page import="java.util.List" %>
<%@ page import="ru.itpark.implementation.model.Auto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@ include file="bootstrap-css.jsp" %>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col">
            <h1>Search Results</h1>

            <div class="row">
                <% if (request.getAttribute("items") != null) { %>
                <% for (Auto item : (List<Auto>) request.getAttribute("items")) { %>
                <div class="col-sm-6 mt-3">
                    <div class="card">
                        <a href="<%= request.getContextPath()%>/details/<%= item.getId() %>">
                            <img src="<%= request.getContextPath() %>/images/<%= item.getImage() %>"
                                 class="card-img-top" alt="<%= item.getName() %>">
                        </a>
                        <div class="card-body">
                            <h5 class="card-title"><%= item.getName() %>
                            </h5>
                            <p class="card-text"><%= item.getDescription()%>
                            </p>
                        </div>
                    </div>
                </div>
                <% } %>
                <% } %>
            </div>

        </div>
    </div>
</div>
<%@ include file="bootstrap-scripts.jsp" %>
</body>
</html>
