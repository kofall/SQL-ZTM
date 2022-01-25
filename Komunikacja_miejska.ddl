-- Generated by Oracle SQL Developer Data Modeler 20.4.0.374.0801
--   at:        2021-12-28 19:30:58 CET
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

DROP TABLE BILET cascade constraints;
DROP TABLE GODZINY_ODJAZDOW cascade constraints;
DROP TABLE KIEROWCA cascade constraints;
DROP TABLE KONTO cascade constraints;
DROP TABLE LINIA cascade constraints;
DROP TABLE POJAZD cascade constraints;
DROP TABLE PRZEJAZDY cascade constraints;
DROP TABLE PRZYSTANEK cascade constraints;
DROP TABLE PRZYSTANEK_W_TRASIE cascade constraints;
DROP TABLE RODZAJE_BILETOW cascade constraints;
DROP TABLE ULGI cascade constraints;
DROP TABLE WPIS_HISTORII cascade constraints;
DROP SEQUENCE id_biletu_SEQ;
DROP SEQUENCE nr_linii_SEQ;
DROP SEQUENCE id_przystanku_w_trasie_SEQ;
DROP SEQUENCE id_klienta_SEQ;
DROP FUNCTION cenaBiletu;

CREATE TABLE bilet (
    id_biletu                   INTEGER NOT NULL,
    moment_skasowania_poczatek  TIMESTAMP,
    moment_skasowania_koniec    TIMESTAMP,
    rodzaje_biletow_nazwa       VARCHAR2(15 CHAR) NOT NULL,
    ulgi_nazwa                  VARCHAR2(15) NOT NULL,
    id_przystanku_koniec        INTEGER,
    id_przystanku_poczatek      INTEGER
);

ALTER TABLE bilet ADD CONSTRAINT bilety_pk PRIMARY KEY ( id_biletu );

CREATE TABLE godziny_odjazdow (
    godzina_odjazdu             TIMESTAMP NOT NULL,
    id_przystanku_w_trasie_szt  INTEGER NOT NULL
);

ALTER TABLE godziny_odjazdow ADD CONSTRAINT godziny_odjazdow_pk PRIMARY KEY ( godzina_odjazdu,
                                                                              id_przystanku_w_trasie_szt );

CREATE TABLE kierowca (
    pesel     VARCHAR2(11 CHAR) NOT NULL,
    imie      VARCHAR2(13 CHAR) NOT NULL,
    nazwisko  VARCHAR2(30 CHAR) NOT NULL
);

ALTER TABLE kierowca ADD CONSTRAINT kierowcy_pk PRIMARY KEY ( pesel );

CREATE TABLE konto (
    id_konta  INTEGER NOT NULL,
    login     VARCHAR2(20 CHAR) NOT NULL,
    haslo     VARCHAR2(20 CHAR) NOT NULL,
    e_mail    VARCHAR2(254 CHAR) NOT NULL,
    imie      VARCHAR2(13 CHAR),
    nazwisko  VARCHAR2(30 CHAR),
    typ       CHAR(1 CHAR) NOT NULL
);

ALTER TABLE konto ADD CONSTRAINT konto_pk PRIMARY KEY ( id_konta,
                                                        login );

CREATE TABLE linia (
    nr_lini            INTEGER NOT NULL,
    godziny_dzialania  VARCHAR2(11) NOT NULL
);

ALTER TABLE linia ADD CONSTRAINT linia_pk PRIMARY KEY ( nr_lini );

CREATE TABLE pojazd (
    numer_seryjny             VARCHAR2(10 CHAR) NOT NULL,
    liczba_miejsc_stojacych   INTEGER,
    liczba_miejsc_siedzacych  INTEGER,
    typ_pojazdu               CHAR(1 CHAR) NOT NULL,
    nr_rejestracyjny          VARCHAR2(7 CHAR)
);

ALTER TABLE pojazd ADD CONSTRAINT pojazd_pk PRIMARY KEY ( numer_seryjny );

CREATE TABLE przejazdy (
    data_rozpoczecia      TIMESTAMP NOT NULL,
    data_zakonczenia      TIMESTAMP NOT NULL,
    kierowca_pesel        VARCHAR2(11 CHAR) NOT NULL,
    pojazd_numer_seryjny  VARCHAR2(10 CHAR) NOT NULL,
    linia_nr_lini         INTEGER NOT NULL
);

ALTER TABLE przejazdy
    ADD CONSTRAINT przejazdy_pk PRIMARY KEY ( data_rozpoczecia,
                                              data_zakonczenia,
                                              kierowca_pesel,
                                              pojazd_numer_seryjny,
                                              linia_nr_lini );

CREATE TABLE przystanek (
    nazwa       VARCHAR2(20 CHAR) NOT NULL,
    rodzaj      VARCHAR2(11 CHAR),
    zadaszenie  CHAR(1 CHAR)
);

ALTER TABLE przystanek ADD CONSTRAINT przystanki_pk PRIMARY KEY ( nazwa );

CREATE TABLE przystanek_w_trasie (
    nr_w_trasie                 INTEGER NOT NULL,
    na_zadanie                  CHAR(1 CHAR) NOT NULL,
    linia_nr_lini               INTEGER NOT NULL,
    przystanek_nazwa            VARCHAR2(20 CHAR) NOT NULL,
    id_przystanku_w_trasie_szt  INTEGER NOT NULL
);

ALTER TABLE przystanek_w_trasie ADD CONSTRAINT przystanki_w_trasie_pk PRIMARY KEY ( id_przystanku_w_trasie_szt );

ALTER TABLE przystanek_w_trasie
    ADD CONSTRAINT przystanki_w_trasie__un UNIQUE ( nr_w_trasie,
                                                    linia_nr_lini,
                                                    przystanek_nazwa );

CREATE TABLE rodzaje_biletow (
    nazwa               VARCHAR2(15 CHAR) NOT NULL,
    czas_obowiazywania  INTERVAL DAY(9) TO SECOND(0) NOT NULL,
    strefa              CHAR(1 CHAR) NOT NULL,
    cena_normalna       FLOAT NOT NULL
);

ALTER TABLE rodzaje_biletow ADD CONSTRAINT rodzaje_biletow_pk PRIMARY KEY ( nazwa );

CREATE TABLE ulgi (
    nazwa               VARCHAR2(15) NOT NULL,
    wartosc_procentowa  FLOAT NOT NULL
);

ALTER TABLE ulgi ADD CONSTRAINT ulgi_pk PRIMARY KEY ( nazwa );

CREATE TABLE wpis_historii (
    numer_wpisu      INTEGER NOT NULL,
    data             TIMESTAMP NOT NULL,
    opis             CLOB,
    konto_id_konta   INTEGER NOT NULL,
    konto_login      VARCHAR2(20 CHAR) NOT NULL,
    bilet_id_biletu  INTEGER NOT NULL
);

ALTER TABLE wpis_historii
    ADD CONSTRAINT wpisy_historii_pk PRIMARY KEY ( numer_wpisu,
                                                   konto_id_konta,
                                                   konto_login );

ALTER TABLE bilet
    ADD CONSTRAINT bilety_przystanki_w_trasie_fk FOREIGN KEY ( id_przystanku_poczatek )
        REFERENCES przystanek_w_trasie ( id_przystanku_w_trasie_szt );

ALTER TABLE bilet
    ADD CONSTRAINT bilety_przystanki_w_trasie_fk2 FOREIGN KEY ( id_przystanku_koniec )
        REFERENCES przystanek_w_trasie ( id_przystanku_w_trasie_szt );

ALTER TABLE bilet
    ADD CONSTRAINT bilety_rodzaje_biletow_fk FOREIGN KEY ( rodzaje_biletow_nazwa )
        REFERENCES rodzaje_biletow ( nazwa );

ALTER TABLE bilet
    ADD CONSTRAINT bilety_ulgi_fk FOREIGN KEY ( ulgi_nazwa )
        REFERENCES ulgi ( nazwa );

ALTER TABLE godziny_odjazdow
    ADD CONSTRAINT godz_odjazd_przyst_w_trasie_fk FOREIGN KEY ( id_przystanku_w_trasie_szt )
        REFERENCES przystanek_w_trasie ( id_przystanku_w_trasie_szt );

ALTER TABLE przejazdy
    ADD CONSTRAINT przejazdy_kierowcy_fk FOREIGN KEY ( kierowca_pesel )
        REFERENCES kierowca ( pesel );

ALTER TABLE przejazdy
    ADD CONSTRAINT przejazdy_linia_fk FOREIGN KEY ( linia_nr_lini )
        REFERENCES linia ( nr_lini );

ALTER TABLE przejazdy
    ADD CONSTRAINT przejazdy_pojazd_fk FOREIGN KEY ( pojazd_numer_seryjny )
        REFERENCES pojazd ( numer_seryjny );

ALTER TABLE przystanek_w_trasie
    ADD CONSTRAINT przystanki_w_trasie_linie_fk FOREIGN KEY ( linia_nr_lini )
        REFERENCES linia ( nr_lini );

ALTER TABLE przystanek_w_trasie
    ADD CONSTRAINT przystanki_w_trasie_przyst_fk FOREIGN KEY ( przystanek_nazwa )
        REFERENCES przystanek ( nazwa );

ALTER TABLE wpis_historii
    ADD CONSTRAINT wpisy_historii_bilety_fk FOREIGN KEY ( bilet_id_biletu )
        REFERENCES bilet ( id_biletu );

ALTER TABLE wpis_historii
    ADD CONSTRAINT wpisy_historii_konta_fk FOREIGN KEY ( konto_id_konta,
                                                         konto_login )
        REFERENCES konto ( id_konta,
                           login );

--################################################################

CREATE SEQUENCE id_biletu_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE nr_linii_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE id_klienta_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE id_przystanku_w_trasie_SEQ START WITH 1 INCREMENT BY 1;

--################################################################

CREATE OR REPLACE FUNCTION cenaBiletu (pIdBiletu INTEGER) RETURN NUMBER IS
    vWartoscZnizki FLOAT;
    vCenaNormalna FLOAT;
    vRodzaj_biletu VARCHAR2(15 CHAR);
    vNazwa_ulgi VARCHAR2(15 CHAR);
BEGIN
    SELECT Rodzaje_biletow_nazwa, Ulgi_nazwa INTO vRodzaj_biletu, vNazwa_ulgi FROM Bilet WHERE id_biletu = pIdBiletu;
    SELECT cena_normalna INTO vCenaNormalna FROM Rodzaje_biletow WHERE nazwa = vRodzaj_biletu;
    SELECT wartosc_procentowa INTO vNazwa_ulgi FROM Ulgi WHERE nazwa = vNazwa_ulgi;
    RETURN vWartoscZnizki * vCenaNormalna;
END cenaBiletu;

--################################################################

--CREATE OR REPLACE PROCEDURE dodajKlienta (
--    login KONTO.login%TYPE,
--    haslo KONTO.haslo%TYPE,
--    e_mail KONTO.e_mail%TYPE,
--    imie KONTO.imie%TYPE,
--    nazwisko KONTO.nazwisko%TYPE,
--    typ KONTO.typ%TYPE) IS
--BEGIN
--    INSERT INTO KONTO VALUES(
--        id_klienta_SEQ.NEXTVAL,
--        login,
--        haslo,
--        e_mail,
--        imie,
--        nazwisko,
--        typ
--    );
--END dodajKlienta;

--################################################################

-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                            12
-- CREATE INDEX                             0
-- ALTER TABLE                             25
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0