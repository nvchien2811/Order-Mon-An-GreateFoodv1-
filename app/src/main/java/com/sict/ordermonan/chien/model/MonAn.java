package com.sict.ordermonan.chien.model;

import java.io.Serializable;

public class MonAn implements Serializable {
    String id;
    String ten;
    String gia;
    String hinhanh;
    String mota;
    float rating;
    int soluong;
    int soluongdanhgia;


    public MonAn(String id, String ten, String gia, String hinhanh, String mota, float rating, int soluong, int soluongdanhgia) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.hinhanh = hinhanh;
        this.mota = mota;
        this.rating = rating;
        this.soluong = soluong;
        this.soluongdanhgia = soluongdanhgia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public MonAn() {
    }

    public int getSoluongdanhgia() {
        return soluongdanhgia;
    }

    public void setSoluongdanhgia(int soluongdanhgia) {
        this.soluongdanhgia = soluongdanhgia;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }
}
