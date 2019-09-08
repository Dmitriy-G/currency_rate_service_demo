--Flyway используется для созания таблиц, и начальных данных

--Сделал код валюты PRIMARY KEY, т.к. не может быть двух валют с одинаковым кодом
CREATE TABLE currency_list
(
    code integer PRIMARY KEY NOT NULL,
    mnemonics varchar NOT NULL,
    description varchar
);

--У записей журнала сейчас нет уникального поля, по которому ее можно идентифицировать, в задаче не требовалось его создание
CREATE TABLE currency_journal
(
    code integer NOT NULL,
    date date NOT NULL,
    rate_buy double precision NOT NULL,
    rate_sell double precision NOT NULL
);

INSERT INTO currency_list ("code", "mnemonics", "description") VALUES (840, 'USD', 'US Dollar');
INSERT INTO currency_list ("code", "mnemonics", "description") VALUES (978, 'EUR', 'Euro');