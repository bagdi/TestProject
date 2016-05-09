<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<link rel="stylesheet" href="css/alertify/alertify.css"/>
<link rel="stylesheet" href="css/fields.css"/>
<link rel="stylesheet" href="css/form.css"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
    <title>Title</title>
</head>
<body>
<fieldset class="form">
    <legend>Search</legend>
    <label for="findLine">Enter: </label>
    <input type="text" id="findLine" size="50" class="field"/>
    <input type="button" id="searchButton" value="Search" class="btn"/>
</fieldset>

<p id="responseData"></p>

<h4><center>&#169;Bogdan Rutskov <a href="http://vk.com/bavuta">
    <img src="images/vk.jpg" width="32" height="32" alt="bavuta"></a></center></h4>

<script type="text/javascript" src="js/jquery-2.2.2.js"></script>
<script type="text/javascript" src="js/alertify/alertify.js"></script>
<script type="text/javascript" src="js/main.js"></script>
</body>
</html>
