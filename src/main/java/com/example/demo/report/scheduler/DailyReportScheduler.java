package com.example.demo.report.scheduler;

import com.example.demo.report.dto.DailyReport;
import com.example.demo.report.service.DailyReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Log4j2
@Service
public class DailyReportScheduler {

    private final DailyReportService dailyReportService;

    public DailyReportScheduler(DailyReportService dailyReportService){
        this.dailyReportService = dailyReportService;
    }

    @Scheduled(cron = "0 50 17 * * *", zone = "Europe/Istanbul")
    public void sendDailyReport() {
        LocalDate today = LocalDate.now();
        DailyReport report = dailyReportService.buildReportFor(today);
        dailyReportService.sendDailyReport(report);
        log.info("Daily report sent successfully");
    }
}
