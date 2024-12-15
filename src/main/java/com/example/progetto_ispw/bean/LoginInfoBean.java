package com.example.progetto_ispw.bean;

//----BEAN CONTENENTE INFORMAZIONI UTILI PER IL LOGIN----
public class LoginInfoBean {
    //----ATTRIBUTI----
    private String username;
    private String password;
    //----METODI----
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {return password;}
    public String getUsername() {return username;}
}
