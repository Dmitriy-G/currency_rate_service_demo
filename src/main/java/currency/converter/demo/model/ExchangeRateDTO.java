package currency.converter.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class ExchangeRateDTO implements Serializable {
    // используется DTO, т.к. возвращать все поля из журнала не нужно
    // здесь не нужна возможность изменения полей, поэтому сеттеров нет
    private Double rateBuy;
    private Double rateSell;

    public ExchangeRateDTO(Double rateBuy, Double rateSell) {
        this.rateBuy = rateBuy;
        this.rateSell = rateSell;
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
        ExchangeRateDTO that = (ExchangeRateDTO) o;
        return Objects.equals(rateBuy, that.rateBuy) &&
                Objects.equals(rateSell, that.rateSell);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rateBuy, rateSell);
    }
}
