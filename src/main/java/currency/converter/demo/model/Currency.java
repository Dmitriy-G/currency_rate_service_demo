package currency.converter.demo.model;

import java.util.Objects;

public class Currency {

    private String mnemonics;
    private Integer code;
    private String description;

    public Currency(String mnemonics, Integer code, String description) {
        this.mnemonics = mnemonics;
        this.code = code;
        this.description = description;
    }

    public String getMnemonics() {
        return mnemonics;
    }

    public void setMnemonics(String mnemonics) {
        this.mnemonics = mnemonics;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(mnemonics, currency.mnemonics) &&
                Objects.equals(code, currency.code) &&
                Objects.equals(description, currency.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mnemonics, code, description);
    }
}
