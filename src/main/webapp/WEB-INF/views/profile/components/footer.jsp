<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</div> <!-- Close row -->
</div> <!-- Close container-fluid -->

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<c:if test="${not empty pageScript}">
  <script src="${pageContext.request.contextPath}/assets/js/${pageScript}"></script>
</c:if>

</body>
</html>