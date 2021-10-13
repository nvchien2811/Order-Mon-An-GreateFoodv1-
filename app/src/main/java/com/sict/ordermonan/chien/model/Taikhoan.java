package com.sict.ordermonan.chien.model;

public class Taikhoan {
    String id;
  String ten;
  String email;
  String sdt;
  String uid;
  String password;
  String diachi;
  String hinhanh;

    public Taikhoan(String id, String ten, String email, String sdt, String uid, String password, String diachi, String hinhanh) {
        this.id = id;
        this.ten = ten;
        this.email = email;
        this.sdt = sdt;
        this.uid = uid;
        this.password = password;
        this.diachi = diachi;
        this.hinhanh = hinhanh;
    }

    public Taikhoan() {
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
