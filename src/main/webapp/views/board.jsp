<%@page isELIgnored="false" %>
<%@ page import="com.study.ebsoft.dto.BoardDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String searchConditionQueryString = (String) request.getAttribute("searchConditionQueryString");
    BoardDTO board = (BoardDTO) request.getAttribute("board");
%>
<jsp:include page="/include/header.jsp" flush="false">
    <jsp:param name="css_path" value="board_view.css"/>
    <jsp:param name="js_path" value="board_view.js"/>
</jsp:include>
<body>
<div class="con_title">
    <h2>자유 게시판 - 목록</h2>
</div>
<div class="contents1">
    <div class="con_title" style="margin-top: 40px;">
        <strong><%= board.getWriter() %></strong>
        <p style="margin: 0px; text-align: right">
            <c:set var = "regdate" value="<%=board.getRegDate()%>"/>
            <fmt:parseDate value="${regdate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            등록 일시: <fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${ parsedDateTime }" />
            |
            <% if (board.getModDate() == null) { %>
            수정 일시: -
            <% } else { %>
            <c:set var = "moddate" value="<%=board.getModDate()%>"/>
            <fmt:parseDate value="${moddate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            수정 일시: <fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${ parsedDateTime }" />
            <%= board.getModDate() %><% } %>
        </p>
    </div>
    <div class="contents_sub">
        <!--게시판-->
        <div class="board_view">
            <table>
                <tr>
                    <td width="15%">[<%=board.getCategory() %>]</td>
                    <td><%=board.getTitle() %>
                    </td>
                    <td width="15%">조회수 : <%=board.getHit() %>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" height="200" valign="top" style="padding:20px; line-height:160%">
                        <p><%=board.getContent() %>
                        </p>
                        <hr>
                        <div id="bbs_file_wrap">
                            <div>
                                <c:forEach items="<%=board.getFiles()%>" var="file">
                                    <p>
                                        <a href="/download?file_idx=${file.fileIdx}">${file.originalFileName}</a>
                                    </p>
                                </c:forEach>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
            <table>
            <c:forEach items="<%=board.getComments()%>" var="comment">
                <form action="/comment/delete<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>" method="post" name="dcfrm" id="dcfrm${comment.commentIdx}">
                    <input type='hidden' name='board_idx' value="<%=board.getBoardIdx()%>">
                    <input type='hidden' name='comment_idx' value="${comment.commentIdx}">
                    <tr>
                        <td class='coment_re'>
                            <strong>${comment.writer}</strong> | ${comment.regDate}
                            <div class='coment_re_txt'>
                                    ${comment.content}
                            </div>
                        </td>
                        <td class='coment_re' width='20%' align='right'>
                            <input type='password' name='password' placeholder='비밀번호' class='coment_input pR10'/>
                            <input type='button' value='삭제'
                                   class='btn_comment btn_txt02 delete-button' style='cursor: pointer;'/>
                        </td>
                    </tr>
                </form>
                <c:if test="${not empty error}">
                    <p>Error Message: ${error}</p>
                </c:if>
            </c:forEach>
            </table>
            <form action="/comment/write<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>" method="post" name="cfrm">
            <input type="hidden" name="board_idx" value="<%=board.getBoardIdx() %>">
                <table>
                    <tr>
                        <td width="94%" class="coment_re">
                            글쓴이 <input type="text" name="comment_writer" maxlength="5" class="coment_input"/>&nbsp;&nbsp;
                            비밀번호 <input type="password" name="comment_password" class="coment_input pR10"/>&nbsp;&nbsp;
                        </td>
                        <td width="6%" class="bg01"></td>
                    </tr>
                    <tr>
                        <td class="bg01">
                            <textarea name="comment_content" cols="" rows="" class="coment_input_text"></textarea>
                        </td>
                        <td align="right" class="bg01">
                            <input id="cbtn" type="button" value="댓글등록" class="btn_comment btn_txt02" style="height: 60px; display: block;">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="btn_area">
            <div style="text-align: center; margin: 0 auto;">
                <input type="button" value="목록" class="btn_list btn_txt02" style="cursor: pointer;"
                       onclick="location.href='/boards<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>'" />
                <input type="button" value="수정" class="btn_list btn_txt02" style="cursor: pointer;"
                       onclick="location.href='/board/modify?board_idx=<%= board.getBoardIdx() %><%= searchConditionQueryString.isEmpty() ? "" : "&" + searchConditionQueryString %>'" />
                <input type="button" value="삭제" class="btn_write btn_txt01" style="cursor: pointer;"
                       onclick="location.href='/board/delete?board_idx=<%= board.getBoardIdx() %><%= searchConditionQueryString.isEmpty() ? "" : "&" + searchConditionQueryString %>'" />

            </div>
        </div>
        <!--//게시판-->
    </div>
    <!-- 하단 디자인 -->
</div>

</body>
</html>