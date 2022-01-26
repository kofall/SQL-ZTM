package com.example.ztm;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

public class Wybrane {
    private final SimpleStringProperty rodzaje_biletow_nazwa;
    private final SimpleStringProperty ulgi_nazwa;
    private final TextField ilosc;
    private final SimpleFloatProperty koszt;
    private final SimpleFloatProperty znizka;

    public Wybrane(String rodzaje_biletow_nazwa, String ulgi_nazwa, String ilosc, String koszt, String znizka) {
        this.rodzaje_biletow_nazwa = new SimpleStringProperty(rodzaje_biletow_nazwa);
        this.ulgi_nazwa = new SimpleStringProperty(ulgi_nazwa);
        this.ilosc = new TextField(ilosc);
        this.koszt = new SimpleFloatProperty(Float.valueOf(koszt));
        this.znizka = new SimpleFloatProperty(Float.valueOf(znizka));
    }

    public String getRodzaje_biletow_nazwa() {
        return rodzaje_biletow_nazwa.get();
    }

    public SimpleStringProperty rodzaje_biletow_nazwaProperty() {
        return rodzaje_biletow_nazwa;
    }

    public void setRodzaje_biletow_nazwa(String rodzaje_biletow_nazwa) {
        this.rodzaje_biletow_nazwa.set(rodzaje_biletow_nazwa);
    }

    public String getUlgi_nazwa() {
        return ulgi_nazwa.get();
    }

    public SimpleStringProperty ulgi_nazwaProperty() {
        return ulgi_nazwa;
    }

    public void setUlgi_nazwa(String ulgi_nazwa) {
        this.ulgi_nazwa.set(ulgi_nazwa);
    }

    public TextField getIlosc() {
        return ilosc;
    }

    public float getKoszt() {
        return koszt.get();
    }

    public SimpleFloatProperty kosztProperty() {
        return koszt;
    }

    public void setKoszt(float koszt) {
        this.koszt.set(koszt);
    }

    public float getZnizka() {
        return znizka.get();
    }

    public SimpleFloatProperty znizkaProperty() {
        return znizka;
    }

    public void setZnizka(float znizka) {
        this.znizka.set(znizka);
    }
}
