package com.example.passwordmanger;

public class Password {
    private String id;
    private String desc;
    private String pass;

    public Password(String id, String desc, String pass) {
        this.id = id;
        this.desc = desc;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
