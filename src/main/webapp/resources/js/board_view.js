window.onload = function () {
    document.getElementById("cbtn").onclick = function () {

        if (document.cfrm.comment_writer.value.trim() === '') {
            alert("글쓴이를 입력하셔야 합니다.");
            return false;
        }

        if (document.cfrm.comment_password.value.trim() === '') {
            alert("비밀번호를 입력하셔야 합니다.");
            return false;
        }

        if (document.cfrm.comment_content.value.trim() === '') {
            alert("내용을 입력하셔야 합니다.");
            return false;
        }

        document.cfrm.submit();

    };

    $('.delete-button').on('click', function() {
        if (document.dcfrm.password.value.trim() === '') {
            alert("비밀번호를 입력하셔야 합니다.");
            return false;
        }
        document.dcfrm.submit();
    });
}