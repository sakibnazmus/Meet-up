package com.example.meet_up;

import java.util.HashMap;
import java.util.Map;

public class Group {
    private String admin;
    private Map<String, Boolean> users;
    public Group(String admin) {
        this.admin = admin;
        this.users = new HashMap<>();
        addUser(admin);
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void addUser(String user) {
        users.put(user, false);
    }

    public Boolean getEnabled(String user) {
        return users.get(user);
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }
}
