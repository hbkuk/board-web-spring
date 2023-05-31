<%@ page import="com.study.ebsoft.dto.BoardDTO" %>
<%@page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String searchConditionQueryString = request.getAttribute("searchConditionQueryString").toString();
    BoardDTO board = (BoardDTO) request.getAttribute("board");
%>

<jsp:include page="/include/header.jsp" flush="false">
    <jsp:param name="css_path" value="board_write.css"/>
    <jsp:param name="js_path" value="board_modify_view.js"/>
</jsp:include>
<body>

<div class="contents1">
    <h1>게시판 - 등록</h1>
    <form action="/board/modify<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>" method="post" name="wfrm" enctype="multipart/form-data">
    <input type="hidden" name="board_idx" value="<%=board.getBoardIdx()%>" />
        <input type="hidden" name="category_idx" value="<%=board.getCategoryIdx()%>" />
        <div class="contents_sub" style="margin-top: 50px;">
            <!--게시판-->
            <div class="board_write">
                <h3>기본 정보</h3>
                <table>
                    <tr>
                        <th class="top">카테고리</th>
                        <td class="top" colspan="3">
                            <%=board.getCategory()%>
                        </td>
                    </tr>
                    <tr>
                        <th class="top">등록 일시</th>
                        <td class="top" colspan="3">
                            <c:set var = "regdate" value="<%=board.getRegDate()%>"/>
                            <fmt:parseDate value="${regdate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                            <fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${ parsedDateTime }" />
                        </td>
                    </tr>
                    <tr>
                        <th class="top">수정 일시</th>
                        <td class="top" colspan="3">
                            <% if (board.getModDate() == null) { %>
                            -
                            <% } else { %>
                            <c:set var = "moddate" value="<%=board.getModDate()%>"/>
                            <fmt:parseDate value="${moddate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                            <fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${ parsedDateTime }" />
                            <%= board.getModDate() %><% } %>
                        </td>
                    </tr>
                    <tr>
                        <th class="top">조회수</th>
                        <td class="top" colspan="3"><%=board.getHit()%></td>
                    </tr>
                    <tr>
                        <th class="top">작성자</th>
                        <td class="top" colspan="3"><input type="text" name="writer" value="<%=board.getWriter()%>"
                                                           class="board_view_input_mail" maxlength="4" style="width: 500px"/></td>
                    </tr>
                    <tr>
                        <th>비밀번호</th>
                        <td colspan="3">
                            <input type="password" name="password" value="" class="board_view_input_mail" placeholder="비밀번호" style="width: 500px"/>
                        </td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td colspan="3"><input type="text" name="title" value="<%=board.getTitle() %>" class="board_view_input" style="width: 500px"/></td>
                    </tr>
                    <tr>
                        <th>내용</th>
                        <td colspan="3">
                            <textarea name="content" class="board_editor_area"><%=board.getTitle()%></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th>이미지</th>
                        <td colspan="3">
                            <c:forEach items="<%=board.getFiles()%>" var="file">
                                <div class="upload_file">
                                    <input type="hidden" name="file_idx" value="${file.fileIdx}" class="board_view_input" />
                                    <a href="download.jsp?file_idx=${file.fileIdx}">${file.originalFileName}</a>
                                    <input type="button" class="board_view_delete delete-button" value="delete"/><br/><br/>
                                </div>
                            </c:forEach>
                            <c:forEach begin="<%=board.getFiles().size() + 1%>" end="3" var="index">
                                <input type="file" multiple="multiple" name="upload${index}" class="board_view_input" /><br/><br/>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
            </div>
            <c:if test="${not empty error}">
                <p>Error Message: ${error}</p>
            </c:if>

            <div class="btn_area">
                <div class="align_left">
                    <input type="button" value="취소" class="btn_list btn_txt02" style="cursor: pointer;"
                           onclick="location.href='/boards<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>'">
                </div>
                <div class="align_right">
                    <input id="wbtn" type="button" value="저장" class="btn_write btn_txt01" style="cursor: pointer;"/>
                </div>
            </div>
            <!--//게시판-->
        </div>
    </form>
</div>
<!-- 하단 디자인 -->

</body>
</html>
