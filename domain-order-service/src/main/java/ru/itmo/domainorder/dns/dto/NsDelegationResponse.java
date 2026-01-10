package ru.itmo.domainorder.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NsDelegationResponse {
    private UUID domainId;
    private Boolean isDelegated;
    private List<String> nsServers;
}
