package com.example.ztm;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Koszyk {
    private final SimpleStringProperty rodzaje_biletow_nazwa;
    private final SimpleStringProperty ulgi_nazwa;
    private final SimpleIntegerProperty ilosc;
    private final SimpleFloatProperty koszt;

    public Koszyk(String rodzaje_biletow_nazwa, String ulgi_nazwa, String ilosc, String koszt) {
        this.rodzaje_biletow_nazwa = new SimpleStringProperty(rodzaje_biletow_nazwa);
        this.ulgi_nazwa = new SimpleStringProperty(ulgi_nazwa);
        this.ilosc = new SimpleIntegerProperty(Integer.valueOf(ilosc));
        this.koszt = new SimpleFloatProperty(Float.valueOf(koszt));
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

    public int getIlosc() {
        return ilosc.get();
    }

    public SimpleIntegerProperty iloscProperty() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc.set(ilosc);
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
}
