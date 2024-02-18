package com.dailyReport.utils;


import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

//MySQL PageMaker
@Data
@ToString
@Log4j2
public class PageMaker {

    private int page;
    private int pageSize = 10;
    private int pageStart;
    private int totalCount; //전체 개수
    private int startPage; // 시작 페이지
    private int endPage;   // 끝페이지
    private boolean prev;  // 이전 여부
    private boolean next;  // 다음 여부
    private boolean last;  //마지막 페이지 여부
    private int displayPageNum = 10;  //하단 페이징  << 1 2 3 4 5 6 7 8 9 10 >>
    private int tempEndPage;
    private String searchQuery;

    // 페이징 처리를 위한 Spring Data의 Page 객체 참조
    Page<?> pageObject;

    // 페이지 정보 계산 / startPage, endPage, prev, next 등 값 설정
    private void calcData() {
        endPage = (int) (Math.ceil(page / (double) displayPageNum) * displayPageNum);
        startPage = (endPage - displayPageNum) + 1;

        if (endPage >= tempEndPage) endPage = tempEndPage;
        prev = startPage == 1 ? false : true;
        next = endPage * pageSize >= totalCount ? false : true;
    }

    /**
     * @param pageObject     Page<?> 반환된 리스트값
     * @param pageInt        현재 페이지
     * @param pageSize       페이지사이즈
     * @param displayPageNum 하단 페이징 기본 10설정 << 1 2 3 4 5 6 7 8 9 10 >>
     * @param pageUrl        url 주소
     * @param type           ajax, href =  자바스크립트 ,  링크
     * @return
     */
    public String pageObject(Page<?> pageObject, Integer pageInt,
                             Integer pageSize, Integer displayPageNum, String pageUrl, String type) {
        this.pageObject = pageObject;
        this.page = pageInt == 0 ? 1 : pageInt + 1;
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
        this.tempEndPage = pageObject.getTotalPages();
        log.info("################################tempEndPage {}", tempEndPage);


        if (displayPageNum != null) {
            this.displayPageNum = displayPageNum;
        } else this.displayPageNum = 10;

        this.totalCount = Math.toIntExact(pageObject.getTotalElements());
        log.info("################################pageMaker totalCount {} ", totalCount);
        calcData();

        if (StringUtils.hasText(pageUrl)) {

            // JavaScript를 사용하여 페이지 이동을 처리하는 방식
            // 페이지 새로고침 없이 페이지 컨텐츠를 동적으로 변경하고자 할 때 유용
            // -> 사용자가 페이지 번호를 클릭할 때마다 전체 페이지를 새로 불러오는 대신
            // -> 필요한 데이터만을 서버에서 가져와서 페이지의 일부분만 업데이트하는 방식
            if (type.equalsIgnoreCase("JS")) {
                return paginationJs(pageUrl);
                // 전통적인 HREF 링크를 사용하여 페이지네이션을 처리
                // 사용자가 링크를 클릭하면 새 페이지로 이동
            } else if (type.equalsIgnoreCase("HREF")) {
                return paginationHref(pageUrl);
                // URL의 경로 변수를 사용하는 방식_RESTful URL 구조에 적합
            } else if (type.equalsIgnoreCase("PATHVARIABLE")) {
                return paginationPathVariable(pageUrl);
            }

        }
        return null;
    }

    /**
     * javascript page 버튼 클릭 반환
     *
     * @param url
     * @return
     */
    // JavaScript를 사용하여 페이지 번호 링크 생성
    // 페이지 번호를 클릭 -> 해당 페이지로 이동하는 JavaScript 함수 호출
    // 미사용
    public String paginationJs(String url) {
        StringBuffer sBuffer = new StringBuffer(); // 동적 HTML 구성을 위한 객체 / HTML 문자열 저장
        sBuffer.append("<ul class='pagination justify-content-center'>");
        if (prev) { // 이전 페이지가 존재하는 경우(즉, 현재 페이지 첫 페이지 아닌 경우)
            sBuffer.append("<li class='page-item' ><a class='page-link' onclick='javascript:page(0)' >처음</a></li>");
        } // 처음, 이전 링크 HTML에 추가

        if (prev) {
            sBuffer.append("<li class='page-item'><a  class='page-link' onclick='javascript:page(" + (startPage - 2) + ")';  >&laquo;</a></li>");
        }

        String active = "";
        // 각 페이지 번호에 대한 링크 생성
        for (int i = startPage; i <= endPage; i++) {

            if (page == i) { // 현재 페이지는 active 클래스를 가짐
                active = "class='page-item active'";
            } else {
                active = "class='page-item'";
            }

            sBuffer.append("<li " + active + " >");
            sBuffer.append("<a class='page-link'  onclick='javascript:page(" + (i - 1) + ")'; >" + i + "</a></li>");
            sBuffer.append("</li>");
        }

        // 다음 페이지 링크와 마지막 페이지 링크를 생성
        if (next && endPage > 0 && endPage <= tempEndPage) {
            sBuffer.append("<li class='page-item'><a  class='page-link' onclick='javascript:page(" + (endPage) + ")';  >&raquo;</a></li>");
        }

        if (next && endPage > 0 && !isLast()) {
            sBuffer.append("<li class='page-item'> <a class='page-link' onclick='javascript:page(" + (tempEndPage - 1) + ")'; >마지막</a></li>");
        }
        sBuffer.append("</ul>");
        return sBuffer.toString();
    }

    // 미사용
    public String makeSearch(int page) {
        UriComponents uriComponents =
                UriComponentsBuilder.newInstance()
                        .queryParam("searchQuery", searchQuery)
                        .queryParam("page", page)
                        .build();
        return uriComponents.toUriString();
    }

    /**
     * 링크 파리미터 반환
     *
     * @param url
     * @return
     */

    // 미사용
    public String paginationHref(String url) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<ul class='pagination justify-content-center'>");
        if (prev) {
            sBuffer.append("<li class='page-item'><a  class='page-link' href='" + url + makeSearch(1) + "'>처음</a></li>");
        }

        if (prev) {
            sBuffer.append("<li class='page-item'><a  class='page-link' href='" + url + makeSearch(startPage - 2) + "'>&laquo;</a></li>");
        }

        String active = "";
        for (int i = startPage; i <= endPage; i++) {

            if (page == i) {
                active = "class='page-item active'";
                sBuffer.append("<li " + active + "  > ");
                sBuffer.append("<a class='page-link' href='javascript:void(0)'>" + i + "</a></li>");
                sBuffer.append("</li>");

            } else {
                active = "class='page-item'";
                sBuffer.append("<li " + active + "  > ");
                sBuffer.append("<a class='page-link' href='" + url + makeSearch(i - 1) + "'>" + i + "</a></li>");
                sBuffer.append("</li>");
            }
        }

        if (next && endPage > 0 && endPage <= tempEndPage) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + makeSearch(endPage) + "'>&raquo;</a></li>");
        }

        if (next && endPage > 0 && !isLast()) {
            sBuffer.append("<li class='page-item'><a  class='page-link' href='" + url + makeSearch(tempEndPage - 1) + "'>마지막</a></li>");
        }

        sBuffer.append("</ul>");
        return sBuffer.toString();
    }

    public String paginationPathVariable(String url) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<ul class='pagination justify-content-center'>");
        if (prev) {
            sBuffer.append("<li class='page-item'><a  class='page-link' href='" + url + (1) + "'>처음</a></li>");
        }

        if (prev) {
            sBuffer.append("<li class='page-item'><a  class='page-link' href='" + url + (startPage - 2) + "'>&laquo;</a></li>");
        }

        String active = "";
        for (int i = startPage; i <= endPage; i++) {
            if (page == i) {
                active = "class='page-item active'";
                sBuffer.append("<li " + active + "  > ");
                sBuffer.append("<a class='page-link' href='javascript:void(0)'>" + i + "</a></li>");
                sBuffer.append("</li>");
            } else {
                active = "class='page-item'";
                sBuffer.append("<li " + active + "  > ");
                sBuffer.append("<a class='page-link' href='" + url + (i - 1) + "'>" + i + "</a></li>");
                sBuffer.append("</li>");
            }
        }

        if (next && endPage > 0 && endPage <= tempEndPage) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + (endPage) + "'>&raquo;</a></li>");
        }

        if (next && endPage > 0 && !isLast()) {
            sBuffer.append("<li class='page-item'><a  class='page-link' href='" + url + (tempEndPage - 1) + "'>마지막</a></li>");
        }

        sBuffer.append("</ul>");
        return sBuffer.toString();
    }
}