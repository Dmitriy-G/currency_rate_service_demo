package currency.converter.demo.util;


import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonWrapper wrapper = (JsonWrapper) o;
        return Objects.equals(currencyCodeA, wrapper.currencyCodeA) &&
                Objects.equals(currencyCodeB, wrapper.currencyCodeB) &&
                Objects.equals(date, wrapper.date) &&
                Objects.equals(rateBuy, wrapper.rateBuy) &&
                Objects.equals(rateSell, wrapper.rateSell);
    }

    @Override
    public int hashCode() {

        return Objects.hash(currencyCodeA, currencyCodeB, date, rateBuy, rateSell);
    }
}
