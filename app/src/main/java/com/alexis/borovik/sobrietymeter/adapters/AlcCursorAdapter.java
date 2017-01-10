package com.alexis.borovik.sobrietymeter.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alexis.borovik.sobrietymeter.Alcohol;
import com.alexis.borovik.sobrietymeter.AlcoholDatabaseHelper;
import com.alexis.borovik.sobrietymeter.R;

public class AlcCursorAdapter extends SimpleCursorAdapter {
    private int layout;
    AlcoholDatabaseHelper alcoholDatabaseHelper;
    SQLiteDatabase db;
    Context context;

    public AlcCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
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
        final ToggleButton likeToggleButton = (ToggleButton) view.findViewById(R.id.likeToggleButton);
        nameTextView.setText(alc.getName());
        volumeTextView.setText((String.valueOf(alc.getVolume()) + " мл"));//danger
        glassImageView.setImageResource(alc.getImagePath());

        if(alc.getFavorite() == 1){
            likeToggleButton.setBackgroundResource(R.drawable.hearts);
        }

        likeToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                UpdateDrinkTask updateDrinkTask = new UpdateDrinkTask();
                if (likeToggleButton.isChecked()){
                    likeToggleButton.setBackgroundResource(R.drawable.hearts);
                    updateDrinkTask.execute(id, 1);
                }else{
                    likeToggleButton.setBackgroundResource(R.drawable.favorite);
                    updateDrinkTask.execute(id, 0);
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                adb.setTitle("Удаление напитка");
                adb.setMessage("Удаление нельзя отменить. Удалить?");
                adb.setIcon(android.R.drawable.ic_delete);
                adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeleteDrinkTask().execute(id);
                    }
                });
                adb.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                adb.create().show();
                return false;
            }
        });
    }

    private void createDatabase() {
        alcoholDatabaseHelper = new AlcoholDatabaseHelper(context);
        db = alcoholDatabaseHelper.getWritableDatabase();
    }

    private void insertLikeToggleButtonFlag(int id, int flag){
        AlcoholDatabaseHelper.onItemSetFlag(db, id, flag);
    }

    class UpdateDrinkTask extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer... integers) {
            createDatabase();
            insertLikeToggleButtonFlag(integers[0], integers[1]);
            return null;
        }
    }

    class  DeleteDrinkTask extends AsyncTask<Integer, Void, Void>{
        SQLiteDatabase db;

        @Override
        protected Void doInBackground(Integer... id) {
            db=AlcoholDatabaseHelper
                    .openWritableDb(context);
            AlcoholDatabaseHelper.removeDrink(db,id[0]);
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            AlcoholDatabaseHelper dbHelper = new AlcoholDatabaseHelper(context);
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"_id", "NAME", "VOLUME",  "VOLUME_OF_ALCOHOL", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    null, null, null, null, null);
            swapCursor(cursor);
            notifyDataSetChanged();
            Toast toast = Toast.makeText(context,
                    "Напиток удалён успешно", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
