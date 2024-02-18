package com.dailyReport.controller;

import com.dailyReport.dto.DailyReportsDto;
import com.dailyReport.dto.DailyReportsFormDto;
import com.dailyReport.service.DailyReportsService;
import com.dailyReport.utils.PageMaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

// MVC 패턴 컨트롤러 역할
@Controller
// 필수 생성자 자동 생성
@RequiredArgsConstructor
// 모든 메서드 기본 URL 경로
@RequestMapping("/dailyReports")
@Log4j2
public class DailyReportsController {

    // 서비스 계층 의존성 주입
    private final DailyReportsService dailyReportsService;

    /**
     * 보고서 작성 페이지
     *
     * @param dailyReportsFormDto
     * @return
     */

    @GetMapping("/write")
    public String writeForm(@ModelAttribute DailyReportsFormDto dailyReportsFormDto)
    {
        return "dailyReports/reportForm";
    }

    /**
     * 저장 : 보고서 작성
     *
     * @param dailyReportsFormDto
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @PostMapping("/write") // 저장
    public String write(@Valid DailyReportsFormDto dailyReportsFormDto, BindingResult bindingResult) throws Exception {

        // 에러 발생 -> 작성 폼으로 리다이렉트
        if (bindingResult.hasErrors())
        {
            return "dailyReports/reportForm";
        }

        dailyReportsService.saveDailyReports(dailyReportsFormDto);
        return "redirect:/dailyReports/list";
    }

    /**
     * 보고서 목록
     *
     * @param dailyReportsDto
     * @param page
     * @param pageMaker
     * @param model
     * @return
     */
    // 페이지네이션 적용 보고서 목록 조회, PageMaker 사용
    @GetMapping(value = {"/list", "/list/{page}"})
    public String list(DailyReportsDto dailyReportsDto, @PathVariable("page") Optional<Integer> page, PageMaker pageMaker, Model model) {
        int pageInt = page.orElse(0);

        Pageable pageable = PageRequest.of(pageInt, 10);
        Page<DailyReportsDto> dailyReportList = dailyReportsService.listDailyReports(pageable);

        String pagination = pageMaker.pageObject(dailyReportList, pageInt, 10, 5, "/dailyReports/list/", "pathVariable");

        log.info("**************{}", pageMaker.toString());

        model.addAttribute("dailyReportList", dailyReportList);
        model.addAttribute("pagination", pagination);
        return "dailyReports/reportList";
    }

    /**
     * 보고서 상세
     *
     * @param model
     * @param reportId
     * @return
     */

    @GetMapping(value = {"/detail", "/detail/{reportId}"})
    public String detail(Model model, @PathVariable(value = "reportId", required = false) Long reportId) {

        if (reportId == null)
        {
            reportId = dailyReportsService.getLastReportId();
        }

        DailyReportsFormDto dailyReportsFormDto = dailyReportsService.getReportDtl(reportId);
        model.addAttribute("dailyReportsFormDto", dailyReportsFormDto);
        return "dailyReports/reportDtl";
    }

    /**
     * 업데이트 처리
     *
     * @param dailyReportsFormDto
     * @param bindingResult
     * @param rttr
     * @return
     * @throws Exception
     */
    @PostMapping("/update")
    public String update(@Valid DailyReportsFormDto dailyReportsFormDto, BindingResult bindingResult, RedirectAttributes rttr) throws Exception
    {
        // @Valid: 데이터 유효성을 검사하고, 오류가 있으면 사용자에게 피드백 제공.
        if (bindingResult.hasErrors()) {
            return "dailyReports/reportForm";
        }

        dailyReportsService.updateDailyReports(dailyReportsFormDto);
        rttr.addFlashAttribute("msg", "업데이트 처리 되었습니다.");
        return "redirect:/dailyReports/detail/" + dailyReportsFormDto.getReportId();
    }

    /**
     * 삭제 처리
     *
     * @param reportId
     * @return
     */
    @DeleteMapping("/delete/{reportId}")
    @ResponseBody
    public ResponseEntity<?> deleteReport(@PathVariable(value = "reportId") Long reportId)
    {
        dailyReportsService.deleteReport(reportId);
        return ResponseEntity.ok().body(reportId);
    }
}