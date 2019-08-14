package com.alansolisflores.realmfull.models;

import com.alansolisflores.realmfull.application.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Person extends RealmObject {

    @PrimaryKey
    private int id;

    private String name;

    //1:N Relation
    private RealmList<Dog> dogRealmList;

    public Person(){}//Realm required

    public Person(String name) {
        this.id = MyApplication.PersonID.incrementAndGet();
        this.name = name;
        this.dogRealmList = new RealmList<Dog>();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Dog> getDogRealmList() {
        return this.dogRealmList;
    }
}
