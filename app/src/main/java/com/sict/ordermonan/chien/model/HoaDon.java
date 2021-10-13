package com.sict.ordermonan.chien.model;

import java.io.Serializable;

public class HoaDon implements Serializable {
     String idhoadon;
     boolean tinhtrang;
     String ngay;
     String thoigian;
     String email;
     String uid;
     boolean dadathang;
     String thanhtoanngay;
     String thanhtoanthoigian;
     int tongtien;

    public HoaDon(String idhoadon, boolean tinhtrang, String ngay, String thoigian, String email, String uid, boolean dadathang, String thanhtoanngay, String thanhtoanthoigian, int tongtien) {
        this.idhoadon = idhoadon;
        this.tinhtrang = tinhtrang;
        this.ngay = ngay;
        this.thoigian = thoigian;
        this.email = email;
        this.uid = uid;
        this.dadathang = dadathang;
        this.thanhtoanngay = thanhtoanngay;
        this.thanhtoanthoigian = thanhtoanthoigian;
        this.tongtien = tongtien;
    }

    public HoaDon() {
    }

    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongtien) {
        this.tongtien = tongtien;
    }

    public String getThanhtoanngay() {
        return thanhtoanngay;
    }

    public void setThanhtoanngay(String thanhtoanngay) {
        this.thanhtoanngay = thanhtoanngay;
    }

    public String getThanhtoanthoigian() {
        return thanhtoanthoigian;
    }

    public void setThanhtoanthoigian(String thanhtoanthoigian) {
        this.thanhtoanthoigian = thanhtoanthoigian;
    }

    public boolean isDadathang() {
        return dadathang;
    }

    public void setDadathang(boolean dadathang) {
        this.dadathang = dadathang;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdhoadon() {
        return idhoadon;
    }

    public void setIdhoadon(String idhoadon) {
        this.idhoadon = idhoadon;
    }

    public boolean isTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(boolean tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
