package currency.converter.demo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import currency.converter.demo.dao.CurrencyConverterDAO;
import currency.converter.demo.model.Currency;
import currency.converter.demo.model.ExchangeRateDTO;
import currency.converter.demo.util.JsonWrapper;
import currency.converter.demo.util.UnixEpochDateTypeAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Service
public class CurrencyConverterService {
    private CurrencyConverterDAO currencyConverterDAO;
    private UnixEpochDateTypeAdapter unixEpochDateTypeAdapter;
    private static final Logger LOGGER = LogManager.getLogger(CurrencyConverterDAO.class);

    // в задании не сказано про курс к другим валютам, кроме UAH, поэтому код целевой валюты сделан константой
    private final Integer UAH_CODE = 980;

    private final String API_URL = "https://api.monobank.ua/bank/currency";

    @Autowired
    public CurrencyConverterService(CurrencyConverterDAO currencyConverterDAO, UnixEpochDateTypeAdapter unixEpochDateTypeAdapter) {
        this.currencyConverterDAO = currencyConverterDAO;
        this.unixEpochDateTypeAdapter = unixEpochDateTypeAdapter;
    }

    @Transactional
    public ExchangeRateDTO getExchangeRate(String mnemonics){
        Currency currency = currencyConverterDAO.getCurrencyFromList(mnemonics);
        if (currency == null) {
            throw new IllegalArgumentException();
        }
        Date currentDate = new Date(System.currentTimeMillis());
        // Если в базе нет записи для текущей даты, вернется null
        ExchangeRateDTO dto = currencyConverterDAO.getRateByDate(currency.getCode(), currentDate);
        if (dto == null) {
            JsonWrapper wrapper = getRateFromExternalService(currency.getCode(), UAH_CODE, API_URL);
            currencyConverterDAO.addNewRateToJournal(
                    wrapper.getCurrencyCodeA(),
                    wrapper.getDate(),
                    wrapper.getRateBuy(),
                    wrapper.getRateSell()
            );
            // из полученных данных генерируется dto
            // можно ограничить количество знаков после точки до двух, но про это не говорилось в задании
            dto = new ExchangeRateDTO(wrapper.getRateBuy(), wrapper.getRateSell());
        }

        return dto;
    }

    // можно вынести этот метод в отдельный util класс, но в рамках этой задачи не вижу смысла создавать еще один класс
    // он принимает два кода валют и url api, и находит в полученном ответе нужный курс
    private JsonWrapper getRateFromExternalService(Integer currencyCodeA, Integer currencyCodeB, String apiURL) {
        InputStream input;
        // GSON для парсинга JSON ответа
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, unixEpochDateTypeAdapter)
                .setDateFormat("yyyy-MM-dd")
                .create();
        JsonWrapper[] data;
        try {
            input = new URL(apiURL).openStream();
            Reader reader = new InputStreamReader(input, "UTF-8");
            data = gson.fromJson(reader, JsonWrapper[].class);
            // Возвращается объект wrapper для нужного курса
            // Если нет нужного объекта, значит api его не вернул, бросается Exception
            return Arrays.stream(Objects.requireNonNull(data)).
                    filter( e -> e.getCurrencyCodeA().equals(currencyCodeA) && e.getCurrencyCodeB().equals(currencyCodeB)).
                    findFirst().orElseThrow(Exception::new);
        } catch (Exception e) {
            // все ошибки здесь, связаны с ответом от стороннего сервиса, возвращается код 502
            // обрабатываются все ошибки, включая runtime, т.к. если здесь что то пошло не так
            // это может привести к некорректным данным в базе
            String info = "Incorrect data from api";
            LOGGER.error(info);
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, info, e);
        }
    }
}
