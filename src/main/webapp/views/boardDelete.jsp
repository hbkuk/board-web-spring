<%@page isELIgnored="false" %>
<%@ page import="com.study.ebsoft.dto.BoardDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String searchConditionQueryString = request.getAttribute("searchConditionQueryString").toString();
    BoardDTO board = (BoardDTO) request.getAttribute("board");
%>

<jsp:include page="/include/header.jsp" flush="false">
    <jsp:param name="css_path" value="board.css"/>
    <jsp:param name="js_path" value="board_delete_view.js"/>
</jsp:include>
<body>
<div class="contents1">
    <form action="/board/delete<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>" method="post" name="dfrm">
    <input type = "hidden" name = "board_idx" value="<%=board.getBoardIdx()%>"/>
        <div class="contents_sub">
            <!--게시판-->
            <div class="board_write">
                <table>
                    <tr>
                        <th class="top">글쓴이</th>
                        <td class="top" colspan="3"><input type="text" name="writer" value="<%=board.getWriter() %>" class="board_view_input_mail" maxlength="5" /></td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td colspan="3"><input type="text" name="title" value="<%=board.getTitle() %>" class="board_view_input" /></td>
                    </tr>
                    <tr>
                        <th>비밀번호</th>
                        <td colspan="3"><input type="password" name="password" value="" class="board_view_input_mail"/></td>
                    </tr>
                </table>
            </div>
            <c:if test="${not empty error}">
                <p>Error Message: ${error}</p>
            </c:if>

            <div class="btn_area">
                <div class="align_left">
                    <input type="button" value="목록" class="btn_list btn_txt02" style="cursor: pointer;"
                           onclick="location.href='/boards<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>'"/>
                </div>
                <div class="align_right">
                    <input id="dbtn" type="button" value="삭제" class="btn_write btn_txt01" style="cursor: pointer;" />
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
