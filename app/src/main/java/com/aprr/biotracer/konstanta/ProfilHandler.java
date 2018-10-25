package com.aprr.biotracer.konstanta;

public class ProfilHandler {

    private String judul;
    private String detail;

    public ProfilHandler(String judul,String detail){
        this.judul = judul;
        this.detail = detail;
    }

    public String getJudul() {
        return judul;
    }

    public String getDetail() {
        return detail;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
