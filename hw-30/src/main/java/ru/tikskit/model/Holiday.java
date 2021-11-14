package ru.tikskit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "holidays")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "monthno")
    @EqualsAndHashCode.Include
    private int monthNo;

    @Column(name = "dayno")
    @EqualsAndHashCode.Include
    private int dayNo;

    @Column(name = "description")
    @EqualsAndHashCode.Include
    private String description;
}
