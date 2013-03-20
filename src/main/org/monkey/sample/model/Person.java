package org.monkey.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Person {

    @JsonProperty private String name;
    @JsonProperty private BigDecimal height;
    @JsonProperty private int age;

    public Person(String name, BigDecimal height, int age) {
        this.name = name;
        this.height = height;
        this.age = age;
    }


}
