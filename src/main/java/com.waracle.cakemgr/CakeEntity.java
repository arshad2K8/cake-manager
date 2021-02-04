package com.waracle.cakemgr;

import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "CAKES", uniqueConstraints = {@UniqueConstraint(columnNames = "ID"), @UniqueConstraint(columnNames = "TITLE")})
public class CakeEntity implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @SequenceGenerator(
            name="CAKES_SEQUENCE_GENERATOR",
            sequenceName="CAKES_SEQ"
    )
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CAKES_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer cakeId;

    @Column(name = "TITLE", unique = true, nullable = false, length = 100)
    private String title;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "IMAGE", nullable = false, length = 300)
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CakeEntity{" +
                "cakeId=" + cakeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}