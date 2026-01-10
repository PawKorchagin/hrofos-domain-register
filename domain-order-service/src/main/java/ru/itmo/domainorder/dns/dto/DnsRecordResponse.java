package ru.itmo.domainorder.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DnsRecordResponse {
    private UUID id;
    private UUID domainId;
    private String recordType;
    private String name;
    private String value;
    private Integer ttl;
    private Integer priority;
    private Integer weight;
    private Integer port;
    private String flags;
    private String tag;
}
