package com.aviyan.vastipatrak.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    @NonNull
    private String token;
    @NonNull
    private String loginId;

}
