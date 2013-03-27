package org.monkey.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class SampleDailyPrice {

    @JsonProperty private String ricCode;
    @JsonProperty private LocalDate tradeDate;
    @JsonProperty private BigDecimal open;
    @JsonProperty private BigDecimal high;
    @JsonProperty private BigDecimal low;
    @JsonProperty private BigDecimal close;
    @JsonProperty private BigDecimal adjClose;

    private SampleDailyPrice() {
    }

    public SampleDailyPrice(String ricCode, LocalDate tradeDate, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal adjClose) {
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

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }
}
