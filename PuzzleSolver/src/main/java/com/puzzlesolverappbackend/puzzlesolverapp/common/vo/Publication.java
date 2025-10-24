package com.puzzlesolverappbackend.puzzlesolverapp.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@NoArgsConstructor
public class Publication {

    @Column(name = "year")
    private String year;

    @Column(name = "month")
    private String month;

    public Publication(String year, String month) {
        this.year  = year;
        this.month = month;
    }
}
