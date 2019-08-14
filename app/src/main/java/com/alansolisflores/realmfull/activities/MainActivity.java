package com.alansolisflores.realmfull.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alansolisflores.realmfull.R;
import com.alansolisflores.realmfull.adapters.CustomAdapter;
import com.alansolisflores.realmfull.application.MyApplication;
import com.alansolisflores.realmfull.models.Dog;
import com.alansolisflores.realmfull.models.Person;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener, View.OnClickListener {

    private Realm realm;

    private RealmResults<Person> personRealmResults;

    private CustomAdapter customAdapter;

    private ListView personListView;

    private FloatingActionButton fabAddPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.personListView = findViewById(R.id.personListView);
        this.fabAddPersons = findViewById(R.id.fabAddPersons);

        this.fabAddPersons.setOnClickListener(this);

        Realm.init(this);

        this.realm = Realm.getDefaultInstance();

        this.personRealmResults = this.getAllPeople();
        this.personRealmResults.addChangeListener(this);

        this.customAdapter
                = new CustomAdapter(R.layout.item_layout,this,this.personRealmResults);

        this.personListView.setAdapter(this.customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.items_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.deleteItem:
                this.deleteAllPeople();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private RealmResults<Person> getAllPeople(){
        return this.realm.where(Person.class).findAll();
    }

    private void deleteAllPeople(){
        this.realm.beginTransaction();
        this.realm.deleteAll();
        this.realm.commitTransaction();

        MyApplication.PersonID = new AtomicInteger();
        MyApplication.DogID = new AtomicInteger();
    }

    private void addPeople(){
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
    }

    @Override
    protected void onDestroy() {
        this.realm.removeAllChangeListeners();
        this.realm.close();
        super.onDestroy();
    }

    @Override
    public void onChange(Object o) {
        this.customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name, please");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,null);
        builder.setView(view);

        final EditText editText = view.findViewById(R.id.nameEditText);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString().trim();

                if(!name.isEmpty()){
                    Person person = new Person(name);

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(person);
                    realm.commitTransaction();
                }else{
                    Toast.makeText(MainActivity.this,"Enter name, please", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        builder.show();
    }
}
