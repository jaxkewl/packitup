package com.marshong.packitup.model;

import java.util.ArrayList;

/**
 * Created by martin on 2/20/2015.
 */
public class Location {
    private String name;
    private ArrayList<Container> containers = new ArrayList<>();
    private Globals.Rooms roomType;

    public Location(String name, Globals.Rooms roomType) {
        setName(name);
        setRoomType(roomType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Globals.Rooms getRoomType() {
        return roomType;
    }

    public void setRoomType(Globals.Rooms roomType) {
        this.roomType = roomType;
    }
}
