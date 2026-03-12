package com.example.demo.service;

import com.example.demo.dto.DailyReport;
import com.example.demo.mapper.IDailyReportMapper;
import com.example.demo.model.DeviceLog;
import com.example.demo.repository.IDeviceLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DailyReportService {

    private final IDeviceLogRepository deviceLogRepository;

    private final IDailyReportMapper dailyReportMapper;

    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    public DailyReportService(IDeviceLogRepository deviceLogRepository, IDailyReportMapper dailyReportMapper, TemplateEngine templateEngine, JavaMailSender javaMailSender){
        this.deviceLogRepository = deviceLogRepository;
        this.dailyReportMapper = dailyReportMapper;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    @Value("${report.mail.to}")
    private String reportToEmail;

    public DailyReport buildReportFor(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        List<DeviceLog> logs = deviceLogRepository.findByRecordTimeBetween(start, end);

        Map<String, List<DeviceLog>> logsByDevice = logs.stream()
                .filter(log -> log.getIdentity() != null && log.getIdentity().getIdentifierForVendor() != null)
                .collect(Collectors.groupingBy(log -> log.getIdentity().getIdentifierForVendor()));

        List<DailyReport.DeviceReportEntry> entries = logsByDevice.values().stream()
                .filter(deviceLogs -> !deviceLogs.isEmpty())
                .map(deviceLogs -> {
                    DailyReport.DeviceReportEntry entry = dailyReportMapper.toDeviceReportEntry(deviceLogs.get(0));
                    List<DailyReport.DeviceReadingRow> readings = deviceLogs.stream()
                            .map(log -> dailyReportMapper.toDeviceReadingRow(log))
                            .collect(Collectors.toList());
                    entry.setReadings(readings);
                    return entry;
                })
                .collect(Collectors.toList());

        DailyReport report = new DailyReport();
        report.setReportDate(date);
        report.setDevices(entries);
        return report;
    }

    public void sendDailyReport(DailyReport report){
        if (report == null) {
            return;
        }
        Context context = new Context();
        context.setVariable("dailyReport", report);
        String html = templateEngine.process("daily-report",context);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setRecipients(MimeMessage.RecipientType.TO, reportToEmail);
            message.setSubject("Günlük cihaz raporu – " + report.getReportDate());
            message.setContent(html, "text/html; charset=UTF-8");
            javaMailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException("Rapor maili gönderilemedi", e);
        }
    }
}
