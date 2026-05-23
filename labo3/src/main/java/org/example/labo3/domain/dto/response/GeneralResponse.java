package org.example.labo3.domain.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class GeneralResponse {
    private String uri;
    private String message;
    private int status;
    private LocalDateTime time;
    private Object data;
}