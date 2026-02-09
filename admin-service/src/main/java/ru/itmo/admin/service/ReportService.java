package ru.itmo.admin.service;

public interface ReportService {

    byte[] generateReport(String jwtToken);
}
