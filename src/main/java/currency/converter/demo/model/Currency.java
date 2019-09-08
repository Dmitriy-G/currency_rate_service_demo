package currency.converter.demo.model;

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
}
