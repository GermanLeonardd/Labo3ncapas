package org.example.labo3.domain.dto.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
public class ApiErrorResponse {
    private Object message;
    private int status;
    private LocalDate time;
    private String uri;
}
