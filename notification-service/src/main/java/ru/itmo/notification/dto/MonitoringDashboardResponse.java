package ru.itmo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringDashboardResponse {
    private long domainsExpiringIn30Days;
    private long domainsExpiringIn14Days;
    private long domainsExpiringIn7Days;
    private long domainsExpiringIn3Days;
    private long domainsExpiringIn1Day;
    private long expiredDomains;
    private long totalActiveDomains;
}
