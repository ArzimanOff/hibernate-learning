package org.arzimanoff.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.arzimanoff.hibernate.converter.BirthdayConverter;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile"})
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "firstname", column = @Column(name = "firstname")),
            @AttributeOverride( name = "lastname", column = @Column(name = "lastname")),
            @AttributeOverride( name = "birthdate", column = @Column(name = "birthdate"))
    })
    private PersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;
}
