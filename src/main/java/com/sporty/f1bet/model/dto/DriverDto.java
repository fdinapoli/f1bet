package com.sporty.f1bet.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DriverDto {
    private Long driverNumber;
    private Long sessionKey;
    private String broadcastName;
    private String fullName;
    private String nameAcronym;
    private String teamName;
    private String teamColour;
    private String firstName;
    private String lastName;
    private String headshotUrl;
    private String countryCode;
    private Integer odds;
}
