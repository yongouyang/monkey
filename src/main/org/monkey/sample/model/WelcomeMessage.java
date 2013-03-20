package org.monkey.sample.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

import java.util.Map;

public class WelcomeMessage {

    // Json marshalling will look at either the annotation or getter (if annotation @JsonProperty is not found)

    @JsonProperty("aPerson") private Person person;
    @JsonProperty("date") private LocalDate today;
    private String message;
    @JsonProperty("data") private Map<String, Object> data;
    @JsonIgnore private int ignoredField;

    public WelcomeMessage(Person person, LocalDate today, String message) {
        this.person = person;
        this.today = today;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
