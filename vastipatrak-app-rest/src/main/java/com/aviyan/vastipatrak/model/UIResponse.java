package com.aviyan.vastipatrak.model;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UIResponse {
    private Object responseData;
    private String successMessage;
    private String errorMessage;
}
