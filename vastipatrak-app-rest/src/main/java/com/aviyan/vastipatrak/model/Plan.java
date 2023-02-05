package com.aviyan.vastipatrak.model;

import lombok.*;

import javax.persistence.OneToOne;

@Setter
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Plan {
    private long id;

    @NonNull
    private String name;
    @NonNull
    private Integer groupLimit;
    @NonNull
    private Integer familyLimit;
    @NonNull
    private Integer loginLimit;
    @NonNull
    private boolean fourEyeCheck;

    @OneToOne(mappedBy = "plan")
    private Group group;
}
