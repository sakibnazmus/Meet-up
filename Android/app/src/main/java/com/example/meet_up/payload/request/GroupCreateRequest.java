package com.example.meet_up.payload.request;

public class GroupCreateRequest {

    private String name;

    public GroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GroupCreateRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
