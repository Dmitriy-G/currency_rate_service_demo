package currency.converter.demo.dao;

import currency.converter.demo.model.Currency;
import currency.converter.demo.model.ExchangeRateDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;


@Repository
public class CurrencyConverterDAO  {

    // я использую JdbcTemplate и dao, вместо JpaRepository
    // в задаче нет требования использовать hibernate, мне он показался здесь излишним

    private JdbcTemplate jdbcTemplate;

    // hibernate здесь нет, и его кешей тоже нет, кеши я реализовал через springframework.cache.annotation
    // я дополнительно ничего не настраивал, это simple cache, который  по умолчанию в spring, ehcache здесь нет
    // кеши не ограничены по времени, можно сделать TTL, но не вижу в этом смысла
    // CACHE_MNEMONICS не очищается, т.к. как я понимаю записи в этой таблице не должны менятся
    // CACHE_RATE очищается при добавлении новой записи в журнал
    private final String CACHE_RATE = "getByDate";
    private final String CACHE_MNEMONICS = "getByMnemonics";

    private static final Logger LOGGER = LogManager.getLogger(CurrencyConverterDAO.class);

    @Autowired
    public CurrencyConverterDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // запрос кешируется по входящим параметрам, если с такими параметрами метод уже вызывался
    // он вернет закешированный результат, и не будет выполнятся
    @Cacheable(cacheNames = CACHE_MNEMONICS)
    public Currency getCurrencyFromList(String mnemonics) {
        // получение валюты из списка
        String sql = "select * from currency_list where mnemonics = ?";
        LOGGER.info("Call getCurrencyFromList method");
        LOGGER.info("Execute sql statement ".concat(sql));
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{mnemonics},
                    (rs, rowNum) -> new Currency(
                            rs.getString("mnemonics"),
                            rs.getInt("code"),
                            rs.getString("description")
                    ));
        } catch (EmptyResultDataAccessException e) {
            // нужно обрабатывать exception
            // EmptyResultDataAccessException возвращает код 500, а нужно 422 Unprocessable Entity
            // от клиента пришли синтаксически корректные данные, которые не поддерживает сервер
            String info = "Not supported mnemonics";
            LOGGER.error(info);
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, info, e);
        }
    }

    // запрос кешируется по входящим параметрам, если с такими параметрами метод уже вызывался
    // он вернет закешированный результат, и не будет выполнятся
    @Cacheable(cacheNames = CACHE_RATE)
    public ExchangeRateDTO getRateByDate(Integer code, Date date) {
        // получение курса из журнала, по дате
        LOGGER.info(date.getTime());
        String sql = "select rate_buy, rate_sell from currency_journal where code = ? and date = ?";
        LOGGER.info("Call getRateByDate method");
        LOGGER.info("Execute sql statement ".concat(sql));
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{code, date},
                    (rs, rowNum) -> new ExchangeRateDTO(rs.getDouble("rate_buy"), rs.getDouble("rate_sell")));
        } catch (EmptyResultDataAccessException e) {
            // если запись не найдена, возвращается null т.к. null ожидается и обрабатывается в сервисе
            LOGGER.info("Rate not found in the journal");
            return null;
        }
    }

    // при добавлении новых данных, кеш очищается
    @CacheEvict(cacheNames = CACHE_RATE)
    public void addNewRateToJournal(Integer code, Date date, Double rateBuy, Double rateSell) {
        // добавляется новая запись в журнал
        String sql = "insert into currency_journal (code, date, rate_buy, rate_sell) VALUES (?, ?, ?, ?);";
        LOGGER.info("Call addNewRateToJournal method");
        LOGGER.info("Execute sql statement ".concat(sql));
        jdbcTemplate.update(sql, code, date, rateBuy, rateSell);
    }
}
