window.onload = function () {
    document.getElementById("wbtn").onclick = function () {

        if (document.wfrm.writer.value.trim() === '') {
            alert("글쓴이를 입력하셔야 합니다.");
            return false;
        }

        if (document.wfrm.title.value.trim() === '') {
            alert("글제목을 입력하셔야 합니다.");
            return false;
        }

        if (document.wfrm.password.value.trim() === '') {
            alert("비밀번호를 입력하셔야 합니다.");
            return false;
        }
        document.wfrm.submit();
    };

    $('.delete-button').on('click', function() {
        $(this).parent('.upload_file').remove();
    });
}