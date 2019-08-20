#Realm

## Transacción

```java
this.realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
        Dog dog1 = new Dog("Ragnar");
        Dog dog2 = new Dog("Linus");

        Person person1 = new Person("Andrea Vega");
        Person person2 = new Person("Alan Solis");

        person1.getDogRealmList().add(dog1);
        person2.getDogRealmList().add(dog2);

        realm.copyToRealmOrUpdate(dog1);
        realm.copyToRealmOrUpdate(dog2);

        realm.copyToRealmOrUpdate(person1);
        realm.copyToRealmOrUpdate(person2);

        personRealmResults = getAllPeople();
    }
});
```

## Implementar listener

Necesitamos implementar la interface **RealmChangeListener** y así podemos notificar a nuestro adaptador 

```java
public class MainActivity extends AppCompatActivity implements RealmChangeListener {

    @Override
    public void onChange(Object o) {
        this.customAdapter.notifyDataSetChanged();
    }
}
```

## Cerrar BD

```java
@Override
protected void onDestroy() {
    this.realm.removeAllChangeListeners();
    this.realm.close();
    super.onDestroy();
}
```

## Manejar AutoIncrement ID

```java
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
```