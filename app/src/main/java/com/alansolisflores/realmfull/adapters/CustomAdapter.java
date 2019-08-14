package com.alansolisflores.realmfull.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alansolisflores.realmfull.R;
import com.alansolisflores.realmfull.models.Person;

import io.realm.RealmResults;

public class CustomAdapter extends BaseAdapter {

    private int layout;

    private Context context;

    private RealmResults<Person> realmResults;

    public CustomAdapter(int layout, Context context, RealmResults<Person> realmResults) {
        this.layout = layout;
        this.context = context;
        this.realmResults = realmResults;
    }

    @Override
    public int getCount() {
        return this.realmResults.size();
    }

    @Override
    public Object getItem(int position) {
        return this.realmResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(this.layout,null);

            holder = new ViewHolder();

            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
            holder.dogTextView = convertView.findViewById(R.id.dogTextView);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String name = this.realmResults.get(position).getName();
        String id = Integer.toString(this.realmResults.get(position).getId());

        if(this.realmResults.get(position).getDogRealmList().size() > 0){
            String dog = this.realmResults.get(position).getDogRealmList().first().getName();
            holder.dogTextView.setText(dog);
        }

        holder.descriptionTextView.setText(name);
        holder.titleTextView.setText(id);

        return convertView;
    }

    public static class ViewHolder{
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView dogTextView;
    }
}
