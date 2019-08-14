package com.alansolisflores.realmfull.application;

import android.app.Application;

import com.alansolisflores.realmfull.models.Dog;
import com.alansolisflores.realmfull.models.Person;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {

    public static AtomicInteger PersonID = new AtomicInteger();

    public static AtomicInteger DogID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);
        Realm realm = Realm.getDefaultInstance();
        this.PersonID = setAtomicId(realm, Person.class);
        this.DogID = setAtomicId(realm, Dog.class);

        realm.close();
    }

    private <T extends RealmObject> AtomicInteger setAtomicId(Realm realm,Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue())
                : new AtomicInteger();
    }
}
