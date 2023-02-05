package com.aviyan.vastipatrak.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Email {

    private long id;

    @NonNull
    private String from;
    @NonNull
    private String subject;
    @NonNull
    private String body;
    @NonNull
    private List<String> toList;
    @NonNull
    private List<String> ccList;
    @NonNull
    private List<String> bccList;
}
