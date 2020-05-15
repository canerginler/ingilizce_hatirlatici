package com.example.mob_ingilizce_hatirlatici;

import androidx.annotation.NonNull;

public class kategori {

    public static final int KELİME = 1;
    public static final int KELİME2 = 2;
    public static final int CUMLE = 3;

    private int id;
    private String name;

    public kategori(){}

    public kategori(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
