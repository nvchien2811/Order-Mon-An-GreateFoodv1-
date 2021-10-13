package com.sict.ordermonan.chien.model;

import java.io.Serializable;

public class BanAn implements Serializable {
    String id;
    String TenBanAn;
    boolean tinhtrang;
    String soghe;

    public BanAn(String id, String tenBanAn, boolean tinhtrang, String soghe) {
        this.id = id;
        TenBanAn = tenBanAn;
        this.tinhtrang = tinhtrang;
        this.soghe = soghe;
    }

    public BanAn() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenBanAn() {
        return TenBanAn;
    }

    public void setTenBanAn(String tenBanAn) {
        TenBanAn = tenBanAn;
    }

    public boolean isTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(boolean tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public String getSoghe() {
        return soghe;
    }

    public void setSoghe(String soghe) {
        this.soghe = soghe;
    }
}
