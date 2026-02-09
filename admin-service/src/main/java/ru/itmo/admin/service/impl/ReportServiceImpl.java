package ru.itmo.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.admin.client.AuthClient;
import ru.itmo.admin.client.DomainStatsClient;
import ru.itmo.admin.service.ReportService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AuthClient authClient;
    private final DomainStatsClient domainStatsClient;

    @Override
    public byte[] generateReport(String jwtToken) {
        long registeredUsers ${DB_USER:***REMOVED***} authClient.getUsersCount(jwtToken);
        DomainStatsClient.DomainStats domainStats ${DB_USER:***REMOVED***} domainStatsClient.getDomainStats(jwtToken);

        String timestamp ${DB_USER:***REMOVED***} LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        StringBuilder sb ${DB_USER:***REMOVED***} new StringBuilder();
        sb.append("# Отчёт администратора\n\n");
        sb.append("**Дата формирования:** ").append(timestamp).append("\n\n");
        sb.append("---\n\n");
        sb.append("## Статистика\n\n");
        sb.append("| Показатель | Значение |\n");
        sb.append("|---|---|\n");
        sb.append("| Количество зарегистрированных пользователей | ").append(registeredUsers).append(" |\n");
        sb.append("| Количество активных пользователей (имеющих хотя бы один домен) | ").append(domainStats.activeUsersCount()).append(" |\n");
        sb.append("| Количество зарегистрированных доменов | ").append(domainStats.registeredDomainsCount()).append(" |\n");
        sb.append("\n---\n\n");
        sb.append("*Отчёт сформирован автоматически сервисом admin-service.*\n");

        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
}
