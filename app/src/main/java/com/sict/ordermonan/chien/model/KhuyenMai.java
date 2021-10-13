package com.sict.ordermonan.chien.model;

public class KhuyenMai {
    String id;
    String makm;
    String sotiengiam;
    String tonglonhon;

    public KhuyenMai(String id, String makm, String sotiengiam, String tonglonhon) {
        this.id = id;
        this.makm = makm;
        this.sotiengiam = sotiengiam;
        this.tonglonhon = tonglonhon;
    }

    public KhuyenMai() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMakm() {
        return makm;
    }

    public void setMakm(String makm) {
        this.makm = makm;
    }

    public String getSotiengiam() {
        return sotiengiam;
    }

    public void setSotiengiam(String sotiengiam) {
        this.sotiengiam = sotiengiam;
    }

    public String getTonglonhon() {
        return tonglonhon;
    }

    public void setTonglonhon(String tonglonhon) {
        this.tonglonhon = tonglonhon;
    }
}
