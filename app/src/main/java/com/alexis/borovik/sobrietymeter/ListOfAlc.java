package com.alexis.borovik.sobrietymeter;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alexis.borovik.sobrietymeter.adapters.AlcCursorAdapter;

public class ListOfAlc extends Fragment {
    ListView listOfAlc;
    AlcCursorAdapter adapter;

    public ListOfAlc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_alc, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listOfAlc = (ListView) getActivity().findViewById(R.id.list_of_alc);

    new SetCursorAsyncTasc().execute();
    }

    class SetCursorAsyncTasc extends AsyncTask<Void, Void, Cursor> {
        SQLiteDatabase db;

        @Override
        protected Cursor doInBackground(Void... voids) {
            AlcoholDatabaseHelper dbHelper = new AlcoholDatabaseHelper(getActivity().getBaseContext());
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"_id", "NAME", "VOLUME",  "VOLUME_OF_ALCOHOL", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    null, null, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            adapter = new AlcCursorAdapter(getActivity().getBaseContext(),
                    R.layout.alc_list_item,
                    cursor, new String[]{"NAME", "VOLUME", "VOLUME_OF_ALCOHOL", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    new int[]{0 , 0 , 0, 0 ,0});
            listOfAlc.setAdapter(adapter);
            db.close();
        }
    }

}
