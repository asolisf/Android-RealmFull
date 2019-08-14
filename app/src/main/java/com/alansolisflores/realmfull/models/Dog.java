package com.alansolisflores.realmfull.models;

import com.alansolisflores.realmfull.application.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Dog extends RealmObject {

    @PrimaryKey
    private int id;

    private String name;

    public Dog(){}//Realm required

    public Dog(String name) {
        this.id = MyApplication.DogID.incrementAndGet();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
