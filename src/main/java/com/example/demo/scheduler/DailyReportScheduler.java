package com.example.demo.scheduler;

import com.example.demo.dto.DailyReport;
import com.example.demo.service.DailyReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Log4j2
@Service
public class DailyReportScheduler {

    @Autowired
    private DailyReportService dailyReportService;

    @Scheduled(cron = "0 0 18 * * *", zone = "Europe/Istanbul")
    public void sendDailyReport() {
        LocalDate today = LocalDate.now();
        DailyReport report = dailyReportService.buildReportFor(today);
        dailyReportService.sendDailyReport(report);
        log.info("Daily report sent successfully");
    }
}
