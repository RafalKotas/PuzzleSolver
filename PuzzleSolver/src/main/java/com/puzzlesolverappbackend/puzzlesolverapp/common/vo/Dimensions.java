package com.puzzlesolverappbackend.puzzlesolverapp.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Dimensions {

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    public Dimensions(Integer height, Integer width) {
        this.height = height;
        this.width  = width;
    }
}
