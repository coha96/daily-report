
//상세보기 페이지 이동
function dailyReportsDetail(e){
    const reportId=e.dataset.reportid;
    console.log(reportId);
    location.href=`/dailyReports/detail/${reportId}`;
}

function deleteDailyReports(e){

    if(confirm("정말 삭제 하시겠습니까?")){
        const reportId=e.dataset.reportid;
        console.log(reportId);

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const url = `/dailyReports/delete/${reportId}`;


        $.ajax({
            url      : url,
            type     : "DELETE",
            contentType : "application/json",
            beforeSend : function(xhr){
                /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                xhr.setRequestHeader(header, token);
            },
            dataType : "json",
            cache   : false,
            success  : function(result, status){
                console.log("성공 ", result, status);
                if(status==="success"){
                    alert("삭제 처리 되었습니다.");
                    location.href='/dailyReports/list';
                }
            },
            error : function(jqXHR, status, error){
                console.log("에러 : ",jqXHR, status, error);

            }
        });

    }


}