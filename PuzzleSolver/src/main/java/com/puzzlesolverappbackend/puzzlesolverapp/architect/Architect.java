package com.puzzlesolverappbackend.puzzlesolverapp.architect;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.SizedPublishedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "architect")
@StaticMetamodel(Architect.class)
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Architect extends SizedPublishedEntity {

    public Architect(String filename, String source, String year, String month,
                     Double difficulty, Integer height, Integer width) {
        super(filename, source, difficulty, height, width, year, month);
    }
}

