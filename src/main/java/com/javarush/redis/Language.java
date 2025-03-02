package com.javarush.redis;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import lombok.*;


@Getter
@Setter
public class Language {
    private String language;
    private Boolean isOfficial;
    private BigDecimal percentage;
}