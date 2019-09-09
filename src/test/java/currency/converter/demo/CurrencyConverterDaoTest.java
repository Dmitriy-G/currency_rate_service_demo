package currency.converter.demo;

import currency.converter.demo.dao.CurrencyConverterDAO;
import currency.converter.demo.model.ExchangeRateDTO;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureEmbeddedDatabase
@FlywayTest(locationsForMigrate = "test/db/migration")
public class CurrencyConverterDaoTest extends AbstractTestNGSpringContextTests {

    // тесты для dao
    // для тестов используется embedded postgres
    // таблицы, и данные создаются в миграции
    // можно придумать больше кейсов, если нужно, можно тестировать контроллер через mockMVC

    @Autowired
    CurrencyConverterDAO currencyConverterDAO;

    // поля с тестовыми данными
    private final Integer USD_CODE = 840;
    private final Date TEST_DATE = Date.from(Instant.parse("2019-09-09T15:23:01Z"));

    private final Double TEST_RATE_BUY = 24.961;
    private final Double TEST_RATE_SELL = 25.2417;


    @Test
    public void getDateFromJournal() {
        // тест получения данных из журнала
        final ExchangeRateDTO data = currencyConverterDAO.getRateByDate(USD_CODE, TEST_DATE);
        assertThat(data).isNotNull();
        assertThat(data.getRateBuy()).isEqualTo(TEST_RATE_BUY);
        assertThat(data.getRateSell()).isEqualTo(TEST_RATE_SELL);
    }
}
