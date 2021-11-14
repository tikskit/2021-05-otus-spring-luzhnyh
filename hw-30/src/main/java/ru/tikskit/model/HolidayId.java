package ru.tikskit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@Data
@ToString
@Embeddable
public class HolidayId implements Serializable {
    @Column(name = "monthno")
    private final int monthNo;
    @Column(name = "dayno")
    private final int dayNo;
}
