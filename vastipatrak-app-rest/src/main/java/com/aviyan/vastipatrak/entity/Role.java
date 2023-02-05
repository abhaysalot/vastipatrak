package com.aviyan.vastipatrak.entity;

import com.aviyan.vastipatrak.constant.RoleName;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Role extends AbstractPersistingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "role_seq")
    @SequenceGenerator(name = "role_seq", allocationSize = 5)
    private long id;

    @Enumerated(EnumType.STRING)
    @NonNull
    private RoleName roleName;

    private boolean active;

    private boolean primaryRole;

    @ManyToOne
    @JoinColumn(name = "login_id", referencedColumnName = "id")
    private Login login;

    @OneToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;
}
