package com.example.ztm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class User {
    private Integer idKonta = null;
    private String username = null;
    private String password = null;

    private ObservableList<Map<String, Object>> koszyk_items = FXCollections.<Map<String, Object>>observableArrayList();

    public User(Integer idKonta, String username, String password) {
        this.idKonta = idKonta;
        this.username = username;
        this.password = password;
    }

    public Integer getIdKonta() {
        return idKonta;
    }

    public void setIdKonta(Integer idKonta) {
        this.idKonta = idKonta;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ObservableList<Map<String, Object>> getKoszyk_items() {
        return koszyk_items;
    }

    public void setKoszyk_items(ObservableList<Map<String, Object>> koszyk_items) {
        this.koszyk_items = koszyk_items;
    }

    public void addToKoszyk_items(ObservableList<Wybrane> new_items) {
        for(Wybrane wybrany : new_items) {
            Map<String, Object> item = new HashMap<>();
            item.put("wybrane_nazwa", wybrany.getRodzaje_biletow_nazwa());
            item.put("wybrane_ulga", wybrany.getUlgi_nazwa());
            item.put("wybrane_ilosc", wybrany.getIlosc().getText());
            item.put("wybrane_koszt", wybrany.getKoszt());
            koszyk_items.add(item);
        }
    }
}
