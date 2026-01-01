<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>${pageTitle} | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="context-path" content="${pageContext.request.contextPath}">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <!-- CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
</head>
<body>

<!-- Biến JavaScript để kiểm tra trạng thái đăng nhập (truyền từ server) -->
<script>
  // Biến toàn cục cho JavaScript
  const contextPath = "${pageContext.request.contextPath}";
  const isLoggedIn = ${not empty sessionScope.user};
</script>

<!-- HEADER -->
<jsp:include page="/header.jsp" />

<div class="container-fluid mt-4 mb-5">
  <div class="row">