</div> <!-- Close row -->
</div> <!-- Close container-fluid -->

<!-- Bootstrap JS -->

<jsp:include page="../../../footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<c:if test="${not empty pageScript}">
  <script src="${pageContext.request.contextPath}/assets/js/${pageScript}"></script>
</c:if>

</body>
</html>