package ru.tikskit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "holidays")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Holiday {
    @Embedded
    @EqualsAndHashCode.Include
    @Id
    private HolidayId id;
    @Column(name = "description")
    private String description;
}
