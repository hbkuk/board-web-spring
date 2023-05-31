<%@ page import="com.study.ebsoft.dto.CategoryDTO" %>
<%@ page import="java.util.List" %>
<%@page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String searchConditionQueryString = request.getAttribute("searchConditionQueryString").toString();
    List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("categories");
%>

<jsp:include page="/include/header.jsp" flush="false">
    <jsp:param name="css_path" value="board_write.css"/>
    <jsp:param name="js_path" value="board_write_view.js"/>
</jsp:include>

<body>
<div class="contents1">
    <h1>게시판 - 등록</h1>
    <form action="/board/write<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>" method="post" name="wfrm" enctype="multipart/form-data">
        <div class="contents_sub" style="margin-top: 50px;">
            <!--게시판-->
            <div class="board_write">
                <h3>기본 정보</h3>
                <table>
                    <tr>
                        <th class="top">카테고리</th>
                        <td class="top" colspan="3">
                            <select id="category" name="category_idx">
                                <option value="all">카테고리 선택</option>
                                <c:forEach items="<%=categories%>" var="category">
                                    <option value="${category.categoryIdx}">${category.category}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th class="top">작성자</th>
                        <td class="top" colspan="3"><input type="text" name="writer" value=""
                                                           class="board_view_input_mail" maxlength="5"/></td>
                    </tr>
                    <tr>
                        <th>비밀번호</th>
                        <td colspan="3">
                            <input type="password" name="password" value="" class="board_view_input_mail" placeholder="비밀번호"/>
                            <input type="password" name="passwordConfirm" value="" class="board_view_input_mail" placeholder="비밀번호 확인"/>
                        </td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td colspan="3"><input type="text" name="title" value="" class="board_view_input"/></td>
                    </tr>
                    <tr>
                        <th>내용</th>
                        <td colspan="3">
                            <textarea name="content" class="board_editor_area"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th>이미지</th>
                        <td colspan="3">
                            <input type="file" multiple="multiple" name="upload1" class="board_view_input"/><br/><br/>
                            <input type="file" multiple="multiple" name="upload2" class="board_view_input"/><br/><br/>
                            <input type="file" multiple="multiple" name="upload3" class="board_view_input"/><br/><br/>
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
                           onclick="location.href='/boards<%= searchConditionQueryString.isEmpty() ? "" : "?" + searchConditionQueryString %>'"/>
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
