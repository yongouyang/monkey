package org.monkey.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Security {

    //    @JsonProperty @ObjectId private String ricCode;
    @JsonProperty private String ricCode;
    @JsonProperty private String name;
    @JsonProperty private long lotSize;


    // used by jackson for unmarshalling
    private Security() {
    }

    public Security(String ricCode, String name, long lotSize) {
        this.ricCode = ricCode;
        this.name = name;
        this.lotSize = lotSize;
    }

    public String getRicCode() {
        return ricCode;
    }

    public String getName() {
        return name;
    }

    public long getLotSize() {
        return lotSize;
    }
}
