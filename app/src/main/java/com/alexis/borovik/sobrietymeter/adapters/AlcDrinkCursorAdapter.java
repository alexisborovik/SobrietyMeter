package com.alexis.borovik.sobrietymeter.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.alexis.borovik.sobrietymeter.Alcohol;
import com.alexis.borovik.sobrietymeter.AlcoholDatabaseHelper;
import com.alexis.borovik.sobrietymeter.R;

/**
 * Created by Alexis on 02.11.2016.
 */

public class AlcDrinkCursorAdapter extends SimpleCursorAdapter {
    private int layout;
    AlcoholDatabaseHelper alcoholDatabaseHelper;
    SQLiteDatabase db;
    Context context;

    public AlcDrinkCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        int volume = Integer.parseInt(cursor.getString(2));
        int volumeOfAlc = Integer.parseInt(cursor.getString(3));
        int imagePath = Integer.parseInt(cursor.getString(4));
        int favorite = Integer.parseInt(cursor.getString(5));
        createViews(new Alcohol(name, volume, volumeOfAlc, imagePath, favorite), view, id);
    }
    private void createViews(Alcohol alc, View view, final int id){
        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView volumeTextView = (TextView) view.findViewById(R.id.volumeTextView);
        ImageView glassImageView = (ImageView) view.findViewById(R.id.glassImageView);

        nameTextView.setText(alc.getName());
        volumeTextView.setText((String.valueOf(alc.getVolume()) + " мл"));//danger
        glassImageView.setImageResource(alc.getImagePath());
    }
}
