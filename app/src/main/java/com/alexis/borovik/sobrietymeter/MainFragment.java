package com.alexis.borovik.sobrietymeter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alexis.borovik.sobrietymeter.adapters.AlcDrinkCursorAdapter;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainFragment extends Fragment {
    AlcDrinkCursorAdapter adapter;
    ListView listOfAlc;
    WaveLoadingView waveLoadingView;
    TextView adviceText;
    Button serviceClearButton;
    String advice[];
    String colors[];
    private double weightAlcoholIn1KG, promile = 0;
    public static final String BROADCAST_ACTION = "com.alexis.borovik.sobrietymeter.change";
    BroadcastReceiver br;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        advice = getResources().getStringArray(R.array.tips);
        colors = getResources().getStringArray(R.array.progressColors);
        serviceClearButton = (Button) getActivity().findViewById(R.id.buttonClearProgress);
        adviceText = (TextView) getActivity().findViewById(R.id.textViewAdvice);
        listOfAlc = (ListView) getActivity().findViewById(R.id.list_of_alc);
        waveLoadingView = ((WaveLoadingView) getActivity().findViewById(R.id.waveLoadingView));
        new SetCursorAsyncTasc().execute();
        aetAlcListListeners();
        setClearButtListeners();
        setWaveWiew();
        addListener();
    }

    private void addListener()
    {
        br=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setWaveWiew();
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(br, intFilt);
    }

    private void setTimer()
    {
        /*
        updTimer = new Timer();
        updTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setWaveWiew();
                    }
                });
            }
        }, 0, 10000);
*/
    }

    private void setClearButtListeners() {
        serviceClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                adb.setTitle("Сброс");
                adb.setMessage("Вы действительно хотите сбросить?");
                adb.setIcon(android.R.drawable.ic_delete);
                adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Preferences pref = new Preferences(getActivity());
                        pref.setWeightOfAlcoholInBody(0);
                        setWaveWiew();
                    }
                });
                adb.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                adb.create().show();
            }
        });
    }

    private void setWaveWiew() {
        Preferences pref = new Preferences(getActivity());
        promile = pref.getWeightOfAlcoholInBody();
        double koef = ((double)
                (pref.getPreferences(Preferences.APP_REFERENCES_Effect_OF_ALCOHOL) * 25 + 50)) / 100;
        waveLoadingView.setProgressValue((int) (koef*promile / 4.0 * 100));
        int ind = Tools.ChooseMessage(promile);
        adviceText.setText(advice[ind]);
        waveLoadingView.setWaveColor(Color.parseColor(colors[ind+1]));
        Log.d("alcScore", "setAlc ^^^ "+waveLoadingView.getProgressValue());
    }

    private void aetAlcListListeners() {
        listOfAlc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SQLiteDatabase db = AlcoholDatabaseHelper.
                        openReadablDb(getActivity().getBaseContext());
                Alcohol currentAlcohol = AlcoholDatabaseHelper.getAlcohol(db, (int) l);
                db.close();
                setAlcoholInBody(currentAlcohol);
                setWaveWiew();
            }
        });
    }

    private void setAlcoholInBody(Alcohol alcohol) {
        Preferences pref = new Preferences(getActivity());

        double m = (double) pref.getPreferences(Preferences.APP_REFERENCES_WEIGHT) * 0.7;
        double mOfalc = alcohol.getVolume() * (alcohol.getVolumeOfAlc()) / 100.0 * 0.79 * 0.9;
        promile = pref.getWeightOfAlcoholInBody();
        promile += (mOfalc / m);
        pref.setWeightOfAlcoholInBody((float) promile);
        waveLoadingView.setProgressValue((int) (promile / 4.0 * 100));
        getActivity().startService(new Intent(getActivity(), AlcoService.class));
    }

    class SetCursorAsyncTasc extends AsyncTask<Void, Void, Cursor> {
        SQLiteDatabase db;

        @Override
        protected Cursor doInBackground(Void... voids) {
            return createDB();
        }

        private Cursor createDB() {
            AlcoholDatabaseHelper dbHelper = new AlcoholDatabaseHelper(getActivity().getBaseContext());
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"_id", "NAME", "VOLUME", "VOLUME_OF_ALCOHOL", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    null, null, null, null, "FAVORITE DESC");
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            adapter = new AlcDrinkCursorAdapter(getActivity().getBaseContext(),
                    R.layout.main_alc_list_item,
                    cursor, new String[]{"NAME", "VOLUME", "VOLUME_OF_ALCOHOL", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    new int[]{0, 0, 0, 0, 0});
            listOfAlc.setAdapter(adapter);
            db.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setWaveWiew();
    }

    @Override
    public void onDestroyView()
    {
        getActivity().unregisterReceiver(br);
        super.onDestroyView();
    }
}
