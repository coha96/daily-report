package com.dailyReport.repository;

import com.dailyReport.entity.DailyReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyReportsRepository extends JpaRepository<DailyReports, Long> {

    @Query(value = " SELECT * FROM ( SELECT * FROM ( SELECT ROWNUM AS SEQ, aa.* FROM " +
            "   ( SELECT dr.* FROM daily_reports dr ORDER BY report_id ASC ) aa ) " +
            " WHERE SEQ >= :seq ) WHERE ROWNUM <= :rownum " , nativeQuery = true)
        // nativeQuery = true 옵션은 SQL을 직접 사용할 때 적용
    List<DailyReports> findAllCustom(@Param("seq") long  seq , @Param("rownum") int rownum );
    // 테이블에서 페이징 처리된 데이터를 조회하기 위해 사용
    // 특정 시작 지점(seq)부터 원하는 개수(rownum)만큼의 보고서 데이터를 가져오는 데 사용

    // 테이블 내 최대 reportId 조회 -> 새로운 보고서를 추가할 때 다음 reportId 값을 결정
    @Query(value = "select max(dr.reportId) from DailyReports dr ")
    long getMaxReportId();
}

/*
findAllCustom 메서드에서 사용된 쿼리는 Oracle 데이터베이스의 ROWNUM을 활용하여 페이징 처리를 구현한다.
Spring Data JPA의 기본 메서드만으로는 특정 데이터베이스의 고유 기능을 활용하는 복잡한 쿼리를 구현하기 어렵기 때문에
이런 경우, @Query 어노테이션을 사용하여 필요한 쿼리를 직접 정의할 수 있다.

프로젝트의 요구 사항에 맞는 특정 로직이 필요할 때, 예를 들어 특정 조건에 따른 데이터 필터링이나 복잡한 조인 조건 등,
리포지토리에 직접 쿼리를 정의함으로써 구현할 수 있다.

일부 경우에는 JPA의 기본 메서드를 사용하는 것보다 SQL을 직접 작성하는 편이 성능적으로 유리할 수 있다.
개발자가 직접 SQL을 최적화하여 데이터베이스와의 통신 효율을 높일 수 있다.

PageMaker 사용과의 차이
: PageMaker는 클라이언트 측에서 페이징 로직을 구현할 때 사용되는 유틸리티 클래스
반면, 리포지토리에서 사용하는 쿼리는 서버 측에서 데이터를 페이징 처리하여 가져오는 로직을 구현한다.
즉, PageMaker는 주로 뷰(view)에서 페이징 처리를 위한 UI를 구성하는 반면,
리포지토리의 쿼리는 실제 데이터베이스에서 데이터를 페이징하여 조회하는 데 사용된다.

리포지토리에 커스텀 쿼리를 정의함으로써, Spring Data JPA가 제공하는 기본 메서드만으로는 충족시키기 어려운 복잡한 요구 사항을 해결할 수 있습니다.
이는 애플리케이션의 유연성을 높여줍니다.

이러한 이유로, 특정 요구 사항이나 성능 최적화, 데이터베이스의 특정 기능을 활용하기 위해 리포지토리 인터페이스에 직접 쿼리를 정의할 필요가 있습니다.
컨트롤러나 서비스 계층에서는 이런 데이터 접근 로직을 직접 다루기보다는, 리포지토리 계층을 통해 데이터를 관리하는 것이 책임 분리의 관점에서도 바람직합니다.
 */