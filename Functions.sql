CREATE OR REPLACE FUNCTION zarejestrujUzytkownika(login_p konto.login%TYPE,
    haslo_p konto.haslo%TYPE, e_mail_p konto.e_mail%TYPE, imie_p konto.imie%TYPE,
    nazwisko_p konto.nazwisko%TYPE) RETURN INTEGER IS
    username_exist INTEGER;
BEGIN
    SELECT COUNT(*) INTO username_exist FROM konto WHERE login = login_p;
    IF username_exist != 1 THEN
        INSERT INTO konto(id_konta,login,haslo,e_mail,imie,nazwisko,typ) VALUES (id_klienta_SEQ.nextval, login_p, haslo_p, e_mail_p, imie_p, nazwisko_p, 'K');
    END IF;
    RETURN username_exist;
END zarejestrujUzytkownika;

CREATE OR REPLACE FUNCTION zalogujUzytkownika(login_p konto.login%TYPE,
    haslo_p konto.haslo%TYPE) RETURN INTEGER IS
    username_exist INTEGER;
BEGIN
    SELECT COUNT(*) INTO username_exist FROM konto WHERE login = login_p AND haslo = haslo_p;
    RETURN username_exist;
END zalogujUzytkownika;

CREATE OR REPLACE FUNCTION getTypUzytkownika(login_p konto.login%TYPE) RETURN CHAR IS
    ret_val CHAR;
    username_exist INTEGER;
BEGIN
    SELECT COUNT(*) INTO username_exist FROM konto WHERE login = login_p;
    IF username_exist = 1 THEN
        SELECT typ INTO ret_val FROM konto WHERE login = login_p;
    ELSE
        ret_val := 'N';
    END IF;
    RETURN ret_val;
END getTypUzytkownika;

CREATE OR REPLACE FUNCTION zmodyfikujKierowce(pesel_p kierowca.pesel%TYPE,
    imie_p kierowca.imie%TYPE, nazwisko_p kierowca.nazwisko%TYPE) RETURN INTEGER IS
    username_exist INTEGER;
BEGIN
    SELECT COUNT(*) INTO username_exist FROM kierowca WHERE pesel = pesel_p;
    IF username_exist = 1 THEN
        UPDATE kierowca SET imie = imie_p, nazwisko=nazwisko_p WHERE pesel = pesel_p;
    END IF;
    RETURN username_exist;
END zmodyfikujKierowce;

CREATE OR REPLACE FUNCTION validatePESEL(pesel_p kierowca.pesel%TYPE) RETURN INTEGER IS
    type wagi_t is varray(11) OF SIMPLE_INTEGER;
    wagi_v wagi_t := wagi_t(1, 3, 7, 9, 1, 3, 7,9,1,3,1);
    checksum SIMPLE_INTEGER := 0;
BEGIN
    IF LENGTH(pesel_p)<>11 THEN
        DBMS_OUTPUT.PUT_LINE('Niepoprawna dlugosc');
        RETURN 0;
    END IF;
    IF NVL(REGEXP_COUNT( pesel_p, '[[:digit:]]'),0)<>11 THEN
        DBMS_OUTPUT.PUT_LINE('Niepoprawna ilosc cyfr');
        RETURN 0;
    END IF;
    FOR i IN 1..wagi_v.COUNT LOOP
        checksum := checksum + TO_NUMBER(SUBSTR(pesel_p,i,1)) * wagi_v(i);
    END LOOP;
    IF MOD(checksum, 10) = 0 THEN
        RETURN 1;
    ELSE
        DBMS_OUTPUT.PUT_LINE('Niepoprawna suma: '||TO_CHAR(checksum));
        RETURN 0;
    END IF;
END validatePESEL;


CREATE OR REPLACE FUNCTION dodajKierowce(pesel_p kierowca.pesel%TYPE,
    imie_p kierowca.imie%TYPE, nazwisko_p kierowca.nazwisko%TYPE) RETURN INTEGER IS
    username_exist INTEGER;
    validPesel INTEGER;
BEGIN
    validPesel := validatePESEL(pesel_p);
    
    SELECT COUNT(*) INTO username_exist FROM kierowca WHERE pesel = pesel_p;
    IF username_exist < 1 THEN
        IF validPesel  = 1 THEN
            INSERT INTO kierowca(pesel,imie,nazwisko) VALUES(pesel_p, imie_p, nazwisko_p);
        ELSE
            username_exist :=2;
        END IF;
    END IF;
    RETURN username_exist;
END dodajKierowce;

CREATE OR REPLACE FUNCTION usunKierowce(pesel_p kierowca.pesel%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM kierowca WHERE pesel = pesel_p;
    IF ret_val = 1 THEN
        DELETE FROM kierowca WHERE pesel=pesel_p;
    END IF;
    RETURN ret_val;
END usunKierowce;

CREATE OR REPLACE FUNCTION dodajPojazd(
numer_seryjny_p pojazd.numer_seryjny%TYPE,
stojace_p pojazd.liczba_miejsc_stojacych%TYPE, 
siedzace_p pojazd.liczba_miejsc_siedzacych%TYPE,
typ_p pojazd.typ_pojazdu%TYPE, 
nr_rejestracyjny_p pojazd.nr_rejestracyjny%TYPE) 
RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM pojazd WHERE numer_seryjny = numer_seryjny_p;
    IF ret_val < 1 THEN
        IF typ_p  = 'A' THEN
            IF (nr_rejestracyjny_p IS NULL OR nr_rejestracyjny_p = '') THEN
                ret_val:=2;
            ELSE
                INSERT INTO pojazd(numer_seryjny, liczba_miejsc_stojacych, 
                liczba_miejsc_siedzacych, typ_pojazdu, nr_rejestracyjny) 
                VALUES(numer_seryjny_p, stojace_p, 
                siedzace_p, typ_p, nr_rejestracyjny_p);
            END IF;
        ELSE
            INSERT INTO pojazd(numer_seryjny, liczba_miejsc_stojacych, 
                liczba_miejsc_siedzacych, typ_pojazdu, nr_rejestracyjny)  
                VALUES(numer_seryjny_p, stojace_p, 
                siedzace_p, typ_p, NULL);
        END IF;
    END IF;
    RETURN ret_val;
END dodajPojazd;

CREATE OR REPLACE FUNCTION zmodyfikujPojazd(
numer_seryjny_p pojazd.numer_seryjny%TYPE,
stojace_p pojazd.liczba_miejsc_stojacych%TYPE, 
siedzace_p pojazd.liczba_miejsc_siedzacych%TYPE,
typ_p pojazd.typ_pojazdu%TYPE, 
nr_rejestracyjny_p pojazd.nr_rejestracyjny%TYPE) 
RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM pojazd WHERE numer_seryjny = numer_seryjny_p;
    IF ret_val = 1 THEN
        IF typ_p  = 'A' THEN
            IF nr_rejestracyjny_p IS NULL OR nr_rejestracyjny_p = '' THEN
                ret_val := 2;
            ELSE
                UPDATE pojazd SET liczba_miejsc_stojacych = stojace_p, liczba_miejsc_siedzacych= siedzace_p,typ_pojazdu =typ_p, nr_rejestracyjny= nr_rejestracyjny_p WHERE numer_seryjny = numer_seryjny_p;
            END IF;
        ELSE
            UPDATE pojazd SET liczba_miejsc_stojacych = stojace_p, liczba_miejsc_siedzacych= siedzace_p,typ_pojazdu =typ_p, nr_rejestracyjny= NULL WHERE numer_seryjny = numer_seryjny_p;
        END IF;
    END IF;
    RETURN ret_val;
END zmodyfikujPojazd;

CREATE OR REPLACE FUNCTION usunPojazd(numer_seryjny_p pojazd.numer_seryjny%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM pojazd WHERE numer_seryjny = numer_seryjny_p;
    IF ret_val = 1 THEN
        DELETE FROM pojazd WHERE numer_seryjny = numer_seryjny_p;
    END IF;
    RETURN ret_val;
END usunPojazd;

CREATE OR REPLACE FUNCTION dodajPrzystanek(nazwa_p przystanek.nazwa%TYPE,
rodzaj_p przystanek.rodzaj%TYPE, zadaszenie_p przystanek.zadaszenie%TYPE) 
RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM przystanek WHERE nazwa = nazwa_p;
    IF ret_val < 1 THEN
        INSERT INTO przystanek(nazwa,rodzaj,zadaszenie) VALUES (nazwa_p, rodzaj_p, zadaszenie_p);
    END IF;
    RETURN ret_val;
END dodajPrzystanek;

CREATE OR REPLACE FUNCTION zmodyfikujPrzystanek(nazwa_p przystanek.nazwa%TYPE,
rodzaj_p przystanek.rodzaj%TYPE, zadaszenie_p przystanek.zadaszenie%TYPE) 
RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM przystanek WHERE nazwa = nazwa_p;
    IF ret_val = 1 THEN
        UPDATE przystanek SET rodzaj=rodzaj_p, zadaszenie = zadaszenie_p WHERE nazwa = nazwa_p;
    END IF;
    RETURN ret_val;
END zmodyfikujPrzystanek;

CREATE OR REPLACE FUNCTION validateGodzinyDzialania(godziny_p linia.godziny_dzialania%TYPE)
RETURN INTEGER IS
start_hour NUMBER;
start_minutes NUMBER;
end_hour NUMBER;
end_minutes NUMBER;
BEGIN
    IF NOT REGEXP_LIKE(godziny_p, '([0-1][0-9]|[2][0-3]):([0-5][0-9])-([0-1][0-9]|[2][0-3]):([0-5][0-9])') THEN
        RETURN 1;
    END IF;
    start_hour := TO_NUMBER(REGEXP_SUBSTR(godziny_p,'([0-1][0-9]|[2][0-3])',1));
    start_minutes := TO_NUMBER(REGEXP_SUBSTR(godziny_p,'([0-5][0-9])',3));
    end_hour := TO_NUMBER(REGEXP_SUBSTR(godziny_p,'([0-1][0-9]|[2][0-3])',7));
    end_minutes := TO_NUMBER(REGEXP_SUBSTR(godziny_p,'([0-5][0-9])',9));
    IF start_hour > end_hour THEN 
        RETURN 2;
    END IF;
    IF start_hour = end_hour AND end_minutes <= start_minutes THEN
        RETURN 2;
    END IF;
    RETURN 0;
END validateGodzinyDzialania;

CREATE OR REPLACE FUNCTION dodajLinie (godziny_dzialania_p linia.godziny_dzialania%TYPE) RETURN INTEGER IS
    ret_val INTEGER := 0;
    validGodziny INTEGER;
BEGIN
    validGodziny := validategodzinydzialania(godziny_dzialania_p);
    ret_val:=validGodziny;
    
    IF validGodziny  = 0 THEN
        INSERT INTO linia(nr_lini, godziny_dzialania) VALUES(nr_linii_SEQ.nextval,godziny_dzialania_p);
    END IF;
   
    RETURN ret_val;
END dodajLinie;

CREATE OR REPLACE FUNCTION zmodyfikujLinie (nr_lini_p linia.nr_lini%TYPE, godziny_dzialania_p linia.godziny_dzialania%TYPE) RETURN INTEGER IS
    ret_val INTEGER := 0;
    validGodziny INTEGER;
BEGIN
    validGodziny := validategodzinydzialania(godziny_dzialania_p);
    ret_val:=validGodziny;
    
    IF validGodziny  = 0 THEN
        UPDATE linia SET godziny_dzialania = godziny_dzialania_p WHERE nr_lini = nr_lini_p;
    END IF;
   
    RETURN ret_val;
END zmodyfikujLinie;
CREATE OR REPLACE FUNCTION dodajUlge(nazwa_p ulgi.nazwa%TYPE, wartosc_procentowa_p ulgi.wartosc_procentowa%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM ulgi WHERE nazwa = nazwa_p;
    IF ret_val = 0 THEN
        INSERT INTO ulgi(nazwa,wartosc_procentowa) VALUES(nazwa_p, wartosc_procentowa_p);
    END IF;
    RETURN ret_val;
END dodajUlge;
CREATE OR REPLACE FUNCTION zmodyfikujUlge(nazwa_p ulgi.nazwa%TYPE, wartosc_procentowa_p ulgi.wartosc_procentowa%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM ulgi WHERE nazwa = nazwa_p;
    IF ret_val = 1 THEN
        UPDATE ulgi SET wartosc_procentowa = wartosc_procentowa_p WHERE nazwa = nazwa_p;
    END IF;
    RETURN ret_val;
END zmodyfikujUlge;

CREATE OR REPLACE FUNCTION dodajPrzystanekWTrasie(nr_lini_p linia.nr_lini%TYPE,  
nazwa_p przystanek.nazwa%TYPE, 
nr_w_trasie_p przystanek_w_trasie.nr_w_trasie%TYPE, 
na_zadanie_p przystanek_w_trasie.na_zadanie%TYPE)
RETURN INTEGER IS 
ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM przystanek_w_trasie WHERE linia_nr_lini = nr_lini_p and przystanek_nazwa = nazwa_p and nr_w_trasie = nr_w_trasie_p;
    IF ret_val =0 THEN
        INSERT INTO przystanek_w_trasie(id_przystanku_w_trasie_szt, linia_nr_lini,przystanek_nazwa,nr_w_trasie, na_zadanie) VALUES(id_przystanku_w_trasie_SEQ.nextval, nr_lini_p, nazwa_p, nr_w_trasie_p, na_zadanie_p);
    END IF;
    RETURN ret_val;
END dodajPrzystanekWTrasie;

CREATE OR REPLACE FUNCTION zmodyfikujPrzystanekWTrasie(
id_przystanku_w_trasie_szt_p przystanek_w_trasie.id_przystanku_w_trasie_szt%TYPE,  
nr_lini_p linia.nr_lini%TYPE,
nazwa_p przystanek.nazwa%TYPE, 
nr_w_trasie_p przystanek_w_trasie.nr_w_trasie%TYPE, 
na_zadanie_p przystanek_w_trasie.na_zadanie%TYPE)
RETURN INTEGER IS 
ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM przystanek_w_trasie WHERE linia_nr_lini = nr_lini_p and przystanek_nazwa = nazwa_p and nr_w_trasie = nr_w_trasie_p;
    IF ret_val =1 THEN
        UPDATE przystanek_w_trasie SET przystanek_nazwa = nazwa_p, nr_w_trasie=nr_w_trasie_p, na_zadanie=na_zadanie_p WHERE id_przystanku_w_trasie_szt=id_przystanku_w_trasie_szt_p;
    END IF;
    RETURN ret_val;
END zmodyfikujPrzystanekWTrasie;

CREATE OR REPLACE FUNCTION dodajGodzineOdjazdu(id_przystanku_w_trasie_p godziny_odjazdow.ID_PRZYSTANKU_W_TRASIE_SZT%TYPE, godzina_odjazdu_p godziny_odjazdow.godzina_odjazdu%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM godziny_odjazdow where id_przystanku_w_trasie_szt = id_przystanku_w_trasie_p and godzina_odjazdu = godzina_odjazdu_p;
    IF ret_val = 0 THEN
        INSERT INTO godziny_odjazdow(ID_PRZYSTANKU_W_TRASIE_SZT, godzina_odjazdu) VALUES (id_przystanku_w_trasie_p, godzina_odjazdu_p);
    END IF;
    RETURN ret_val;
END dodajGodzineOdjazdu;

CREATE OR REPLACE FUNCTION zmodyfikujGodzineOdjazdu(id_przystanku_w_trasie_p godziny_odjazdow.ID_PRZYSTANKU_W_TRASIE_SZT%TYPE, godzina_odjazdu_p godziny_odjazdow.godzina_odjazdu%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM godziny_odjazdow where id_przystanku_w_trasie_szt = id_przystanku_w_trasie_p and godzina_odjazdu = godzina_odjazdu_p;
    IF ret_val = 1 THEN
        UPDATE godziny_odjazdow SET ID_PRZYSTANKU_W_TRASIE_SZT=id_przystanku_w_trasie_p, godzina_odjazdu= godzina_odjazdu_p;
    END IF;
    RETURN ret_val;
END zmodyfikujGodzineOdjazdu;

CREATE OR REPLACE FUNCTION dodajBilet(
MOMENT_SKASOWANIA_POCZATEK_p bilet.MOMENT_SKASOWANIA_POCZATEK%TYPE, 
MOMENT_SKASOWANIA_KONIEC_p bilet.MOMENT_SKASOWANIA_KONIEC%TYPE,
RODZAJE_BILETOW_NAZWA_p bilet.RODZAJE_BILETOW_NAZWA%TYPE,
ULGI_NAZWA_p bilet.ULGI_NAZWA%TYPE,
ID_PRZYSTANKU_KONIEC_p bilet.ID_PRZYSTANKU_KONIEC%TYPE,
ID_PRZYSTANKU_POCZATEK_p bilet.ID_PRZYSTANKU_POCZATEK%TYPE) RETURN INTEGER IS
BEGIN
    INSERT INTO bilet VALUES(ID_BILETU_SEQ.nextval, MOMENT_SKASOWANIA_POCZATEK_p, MOMENT_SKASOWANIA_KONIEC_p, RODZAJE_BILETOW_NAZWA_p, ULGI_NAZWA_p, ID_PRZYSTANKU_KONIEC_p, ID_PRZYSTANKU_POCZATEK_p);
    RETURN id_biletu_seq.currval;
END dodajBilet;

CREATE OR REPLACE FUNCTION zmodyfikujBilet(ID_BILETU_p bilet.ID_BILETU%TYPE,
MOMENT_SKASOWANIA_POCZATEK_p bilet.MOMENT_SKASOWANIA_POCZATEK%TYPE, 
MOMENT_SKASOWANIA_KONIEC_p bilet.MOMENT_SKASOWANIA_KONIEC%TYPE,
RODZAJE_BILETOW_NAZWA_p bilet.RODZAJE_BILETOW_NAZWA%TYPE,
ULGI_NAZWA_p bilet.ULGI_NAZWA%TYPE,
ID_PRZYSTANKU_KONIEC_p bilet.ID_PRZYSTANKU_KONIEC%TYPE,
ID_PRZYSTANKU_POCZATEK_p bilet.ID_PRZYSTANKU_POCZATEK%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM bilet WHERE id_biletu = id_biletu_p;
    IF ret_val = 1 THEN
        UPDATE bilet SET MOMENT_SKASOWANIA_POCZATEK=MOMENT_SKASOWANIA_POCZATEK_p,
        MOMENT_SKASOWANIA_KONIEC=MOMENT_SKASOWANIA_KONIEC_p,
        RODZAJE_BILETOW_NAZWA= RODZAJE_BILETOW_NAZWA_p, 
        ULGI_NAZWA=ULGI_NAZWA_p, 
        ID_PRZYSTANKU_KONIEC=ID_PRZYSTANKU_KONIEC_p,
        ID_PRZYSTANKU_POCZATEK= ID_PRZYSTANKU_POCZATEK_p
        WHERE id_biletu = id_biletu_p;
    END IF;
    return ret_val;
END zmodyfikujBilet;

CREATE OR REPLACE FUNCTION dodajRodzajBiletu(NAZWA_p rodzaje_biletow.NAZWA%TYPE,
CZAS_OBOWIAZYWANIA_p rodzaje_biletow.CZAS_OBOWIAZYWANIA%TYPE,
STREFA_p rodzaje_biletow.STREFA%TYPE,
CENA_NORMALNA_p rodzaje_biletow.CENA_NORMALNA%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM rodzaje_biletow WHERE nazwa = nazwa_p;
    IF ret_val = 0 THEN
        INSERT INTO rodzaje_biletow VALUES(NAZWA_p, CZAS_OBOWIAZYWANIA_p, STREFA_p, CENA_NORMALNA_p);
    END IF;
    RETURN ret_val;
END dodajRodzajBiletu;

CREATE OR REPLACE FUNCTION zmodyfikujRodzajBiletu(NAZWA_p rodzaje_biletow.NAZWA%TYPE,
CZAS_OBOWIAZYWANIA_p rodzaje_biletow.CZAS_OBOWIAZYWANIA%TYPE,
STREFA_p rodzaje_biletow.STREFA%TYPE,
CENA_NORMALNA_p rodzaje_biletow.CENA_NORMALNA%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN
    SELECT COUNT(*) INTO ret_val FROM rodzaje_biletow WHERE nazwa = nazwa_p;
    IF ret_val = 1 THEN
        UPDATE rodzaje_biletow set CZAS_OBOWIAZYWANIA = CZAS_OBOWIAZYWANIA_p,STREFA= STREFA_p, CENA_NORMALNA= CENA_NORMALNA_p;
    END IF;
    RETURN ret_val;
END zmodyfikujRodzajBiletu;

CREATE OR REPLACE FUNCTION dodajPrzejazd(
DATA_ROZPOCZECIA_p przejazdy.DATA_ROZPOCZECIA%TYPE, 
DATA_ZAKONCZENIA_p przejazdy.DATA_ZAKONCZENIA%TYPE,
KIEROWCA_PESEL_p przejazdy.KIEROWCA_PESEL%TYPE,
POJAZD_NUMER_SERYJNY_p przejazdy.POJAZD_NUMER_SERYJNY%TYPE,
LINIA_NR_LINI_p przejazdy.LINIA_NR_LINI%TYPE) RETURN INTEGER IS
    ret_val INTEGER;
BEGIN 
    SELECT COUNT(*) INTO ret_val FROM przejazdy WHERE 
    DATA_ROZPOCZECIA=DATA_ROZPOCZECIA_p AND
    DATA_ZAKONCZENIA=DATA_ZAKONCZENIA_p AND
    KIEROWCA_PESEL=KIEROWCA_PESEL_p AND
    POJAZD_NUMER_SERYJNY=POJAZD_NUMER_SERYJNY_p AND
    LINIA_NR_LINI=LINIA_NR_LINI_p;
    IF data_zakonczenia_p <= data_rozpoczecia_p THEN
            ret_val :=2;
        END IF;
    IF ret_val = 0 THEN 
        INSERT INTO przejazdy VALUES(DATA_ROZPOCZECIA_p,DATA_ZAKONCZENIA_p,KIEROWCA_PESEL_p,POJAZD_NUMER_SERYJNY_p,LINIA_NR_LINI_p);
    END IF;
END dodajPrzejazd;

CREATE OR REPLACE FUNCTION kupBilet(
user_id konto.id_konta%TYPE, 
RODZAJE_BILETOW_NAZWA_p rodzaje_biletow.NAZWA%TYPE, 
ULGI_NAZWA_p bilet.ULGI_NAZWA%TYPE)
RETURN INTEGER IS
    id_biletu INTEGER;
    max_id INTEGER;
    max_wpis INTEGER;
    login_v konto.login%TYPE;
BEGIN
    SELECT MAX(id_biletu) INTO max_id FROM bilet;
    id_biletu := dodajBilet(NULL, NULL,RODZAJE_BILETOW_NAZWA_p, ULGI_NAZWA_p, NULL, NULL);
    IF id_biletu > max_id THEN
        SELECT login INTO login_v FROM konto WHERE id_konta = user_id;
        SELECT MAX(numer_wpisu) INTO max_wpis FROM wpis_historii WHERE konto_id_konta = user_id AND konto_login = login_v;
        INSERT INTO wpis_historii VALUES(max_wpis+1, CURRENT_TIMESTAMP, 'Zakup biletu', user_id, login_v, id_biletu);
    END IF;
    return id_biletu;
END kupBilet;

CREATE OR REPLACE FUNCTION skasujBilet(
user_id konto.id_konta%TYPE, 
id_biletu_p bilet.id_biletu%TYPE)
RETURN INTEGER IS
    bilet_istnieje INTEGER;
    max_wpis INTEGER;
    login_v konto.login%TYPE;
    moment TIMESTAMP;
    poczatek TIMESTAMP;
    koniec TIMESTAMP;
BEGIN
    SELECT COUNT(*) INTO bilet_istnieje FROM bilet WHERE id_biletu = id_biletu_p;
    moment:= CURRENT_TIMESTAMP;
    IF bilet_istnieje = 1 THEN
        SELECT moment_skasowania_poczatek, moment_skasowania_koniec INTO poczatek, koniec FROM bilet where id_biletu = id_biletu_p;
        IF poczatek IS NULL THEN
            UPDATE bilet SET moment_skasowania_poczatek = moment WHERE id_biletu = id_biletu_p;
        ELSIF koniec IS NULL THEN
            UPDATE bilet SET moment_skasowania_koniec = moment WHERE id_biletu = id_biletu_p;
        END IF;
        SELECT login INTO login_v FROM konto WHERE id_konta = user_id;
        SELECT MAX(numer_wpisu) INTO max_wpis FROM wpis_historii WHERE konto_id_konta = user_id AND konto_login = login_v;
        INSERT INTO wpis_historii VALUES(max_wpis+1, CURRENT_TIMESTAMP, 'Skasowanie biletu', user_id, login_v, id_biletu_p);
    END IF;
    return bilet_istnieje;
END skasujBilet;

create or replace procedure test_zam is
 vZm char;
begin
    vZm := validateGodzinyDzialania('13:15-13:14');
    DBMS_OUTPUT.PUT_LINE(vZm);
end test_zam;
INSERT INTO konto(id_konta,login,haslo,e_mail,imie,nazwisko,typ) VALUES (1, 'admin', 'admin', 'admin@admin.com', NULL, NULL, 'A');
SELECT * FROM konto;

INSERT INTO kierowca(PESEL, imie, nazwisko) VALUES ('75071589156','Marian','Kucharski');
INSERT INTO kierowca(PESEL, imie, nazwisko) VALUES ('55070966271','Krystian','Soko³owski');
INSERT INTO kierowca(PESEL, imie, nazwisko) VALUES ('03242213559','Kazimierz','Sobczak');
INSERT INTO kierowca(PESEL, imie, nazwisko) VALUES ('97103127512','Dawid','G³owacki');
INSERT INTO kierowca(PESEL, imie, nazwisko) VALUES ('74030834553','Alfred','Przybylski');
COMMIT;
SELECT * FROM kierowca;
exec test_zam;