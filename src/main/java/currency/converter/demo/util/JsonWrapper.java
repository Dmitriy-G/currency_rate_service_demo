package currency.converter.demo.util;


import java.util.Date;

public class JsonWrapper {
    // сеттеры не нужны в этом классе, его заполняет GSON через reflection
    private Integer currencyCodeA;
    private Integer currencyCodeB;
    private Date date;
    private Double rateBuy;
    private Double rateSell;

    public Integer getCurrencyCodeA() {
        return currencyCodeA;
    }

    public Integer getCurrencyCodeB() {
        return currencyCodeB;
    }

    public Date getDate() {
        return date;
    }

    public Double getRateBuy() {
        return rateBuy;
    }

    public Double getRateSell() {
        return rateSell;
    }
}
