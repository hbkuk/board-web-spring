window.onload = function() {
    document.getElementById( 'dbtn' ).onclick = function() {
        if( document.dfrm.password.value.trim() == '' ) {
            alert( '비밀번호를 입력하셔야 합니다.' );
            return false;
        }
        document.dfrm.submit();
    };
};