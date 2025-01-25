package com.example.progetto_ispw.bean;

public class NotificheInfoBean implements Bean{
    //----ATTRIBUTI----
    private String notifiche;

    //----METODI----
    public String getNotifiche() {
        return notifiche;
    }
    public void addNotifiche(String notifiche){
        this.notifiche = this.notifiche + notifiche;
    }
    public void setNotifiche(String notifiche) {
        this.notifiche = notifiche;
    }
}
