$(document).ready(function() {
    $('#submitButton').click(function(e) {
        e.preventDefault();

        const startDate = $('input[name="start_date"]').val();
        const endDate = $('input[name="end_date"]').val();
        const keyword = $('input[name="keyword"]').val();
        const category = $('#category').val();

        let queryString = '';

        if (startDate) {
            queryString += 'start_date=' + startDate + '&';
        }
        if (endDate) {
            queryString += 'end_date=' + endDate + '&';
        }
        if (keyword) {
            queryString += 'keyword=' + keyword + '&';
        }
        if (category && category !== 'all') {
            queryString += 'category_idx=' + category + '&';
        }

        if (category === 'all') {
            window.location.href = window.location.pathname;
            window.location.reload();
        }

        if (startDate && endDate && startDate > endDate) {
            alert('시작 날짜가 종료 날짜보다 클 수 없습니다.');
            return;
        }

        if (queryString) {
            queryString = queryString.slice(0, -1);

            window.location.href = $('#search').attr('action') + '?' + queryString;
        }
    });

    document.getElementsByClassName('.currentDate').value = new Date().toISOString().substring(0, 10);
});