Данные базы для запуска (используется обычный Postgres не embedded):

1. Название testdb
2. Пользователь testuser
3. Пароль testuser

Запуск:
1. mvn clean install
2. mvn spring-boot:run

Доступные url:

/getExchangeRate - принимает get запрос с параметром mnemonics (строка USD или EUR). usd и Usd работать не будет, в задании не говорилось про нечувствительность к регистру.