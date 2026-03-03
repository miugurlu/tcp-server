package com.example.demo.service;

import com.example.demo.dto.DailyReport;
import com.example.demo.model.DeviceLog;
import com.example.demo.repository.IDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DailyReportService {

    @Autowired
    private IDeviceRepository deviceRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${report.mail.to}")
    private String reportToEmail;

    public DailyReport buildReportFor(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        List<DeviceLog> logs = deviceRepository.findByRecordTimeBetween(start, end);

        Map<String, List<DeviceLog>> logsByDevice = logs.stream()
                .filter(log -> log.getIdentity() != null && log.getIdentity().getIdentifierForVendor() != null)
                .collect(Collectors.groupingBy(log -> log.getIdentity().getIdentifierForVendor()));

        List<DailyReport.DeviceReportEntry> entries = new ArrayList<>();
        for (List<DeviceLog> deviceLogs : logsByDevice.values()) {
            if (deviceLogs.isEmpty()) continue;
            DeviceLog.Identity id = deviceLogs.get(0).getIdentity();

            DailyReport.DeviceReportEntry entry = new DailyReport.DeviceReportEntry();
            entry.setDeviceName(id.getDeviceName());
            entry.setSystemName(id.getSystemName());
            entry.setSystemVersion(id.getSystemVersion());
            entry.setIdentifierForVendor(id.getIdentifierForVendor());

            List<DailyReport.DeviceReadingRow> readings = new ArrayList<>();
            for (DeviceLog log : deviceLogs) {
                DailyReport.DeviceReadingRow row = new DailyReport.DeviceReadingRow();
                if (log.getResources() != null) {
                    row.setPhysicalMemoryGB(log.getResources().getPhysicalMemoryGB());
                    row.setProcessorCountActive(log.getResources().getProcessorCountActive());
                    row.setProcessorCountTotal(log.getResources().getProcessorCountTotal());
                    row.setSystemUptime(log.getResources().getSystemUptime());
                    row.setTotalDiskSpaceGB(log.getResources().getTotalDiskSpaceGB());
                    row.setFreeDiskSpaceGB(log.getResources().getFreeDiskSpaceGB());
                }
                if (log.getPower() != null) {
                    row.setBatteryLevel(log.getPower().getBatteryLevel());
                    row.setBatteryState(log.getPower().getBatteryState());
                    row.setThermalState(log.getPower().getThermalState());
                }
                if (log.getNetwork() != null) {
                    row.setConnectionType(log.getNetwork().getConnectionType());
                }
                row.setRecordTime(log.getRecordTime());
                readings.add(row);
            }
            entry.setReadings(readings);
            entries.add(entry);
        }

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
