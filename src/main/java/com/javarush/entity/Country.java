package com.javarush.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "country", schema = "world")
public class Country {

    @Id
    private Integer id;

    String code;

    @Column(name = "code_2")
    String code2;

    String name;

    Integer continent;

    String region;

    @Column(name = "surface_area")
    BigDecimal surfaceArea;

    @Column(name = "indep_year")
    Short indepYear;

    Integer population;

    @Column(name = "life_expectancy")
    BigDecimal lifeExpectancy;

    BigDecimal gnp;

    @Column(name = "gnpo_id")
    BigDecimal gnpoId;

    @Column(name = "local_name")
    String localName;

    @Column(name = "government_form")
    String governmentForm;

    @Column(name = "head_of_state")
    String headOfState;

    @OneToOne
    @JoinColumn(name = "capital")
    City capital;
}
