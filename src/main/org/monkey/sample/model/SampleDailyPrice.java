package org.monkey.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

public class SampleDailyPrice {

    @JsonProperty private String ricCode;
    @JsonProperty private LocalDate tradeDate;
    @JsonProperty private double open;
    @JsonProperty private double high;
    @JsonProperty private double low;
    @JsonProperty private double close;
    @JsonProperty private double adjClose;

    private SampleDailyPrice() {
    }

    public SampleDailyPrice(String ricCode, LocalDate tradeDate, double open, double high, double low, double close, double adjClose) {
        this.ricCode = ricCode;
        this.tradeDate = tradeDate;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjClose = adjClose;
    }

    public String getRicCode() {
        return ricCode;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getAdjClose() {
        return adjClose;
    }
}
