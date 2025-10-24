package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.SizedPublishedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "slitherlink")
@StaticMetamodel(Slitherlink.class)
@Setter
@Getter
@ToString(callSuper = true)
public class Slitherlink extends SizedPublishedEntity {

    public Slitherlink() {
        
    }

    public Slitherlink(String filename, String source, String year, String month,
                       Double difficulty, Integer height, Integer width) {
        super(filename, source, difficulty, height, width, year, month);
    }
}
