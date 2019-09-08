package currency.converter.demo.controller;

import currency.converter.demo.model.ExchangeRateDTO;
import currency.converter.demo.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/", produces = APPLICATION_JSON_UTF8_VALUE)
public class CurrencyConverterController {

    private final CurrencyConverterService currencyConverterService;

    @Autowired
    public CurrencyConverterController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    @GetMapping(value = "/getExchangeRate")
    public ExchangeRateDTO getExchangeRate(@RequestParam(value = "mnemonics") String mnemonics){
        return currencyConverterService.getExchangeRate(mnemonics);
    }
}
