package ru.tikskit.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@ToString(of = {"login", "role"})
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))
public class User {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "login")
    private String login;
    @Setter
    @Column(name = "pass")
    private String pass;
    @Setter
    @Column(name = "role")
    private String role;
}
