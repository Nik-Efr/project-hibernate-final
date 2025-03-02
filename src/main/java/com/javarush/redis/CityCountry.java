package com.javarush.redis;

import com.javarush.entity.Continent;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import jakarta.persistence.Entity;
import lombok.*;


@Getter
@Setter
public class CityCountry {

    private Integer id;

    private String name;

    private String district;

    private Integer population;

    private String countryCode;

    private String alternativeCountryCode;

    private String countryName;

    private Continent continent;

    private String countryRegion;

    private BigDecimal countrySurfaceArea;

    private Integer countryPopulation;

    private List<Language> languages;

}