package com.alexis.borovik.sobrietymeter;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexis.borovik.sobrietymeter.adapters.MyGridAdapter;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class AddAlcohole extends Fragment {
    private EditText editName;
    private TextView valTextView, procentTextView;
    private DiscreteSeekBar valSeekBar, procentSeek;
    private GridView gridOfImages;
    private Button addNewAlc;
    int volume = 50,  procent = 2, setImagePath = 0;
    Integer[] list = {R.drawable.alc1, R.drawable.alc2, R.drawable.alc3, R.drawable.alc4, R.drawable.alc5,
            R.drawable.alc6, R.drawable.alc7, R.drawable.alc8, R.drawable.alc9, R.drawable.alc10,
            R.drawable.alc11, R.drawable.alc12, R.drawable.alc13, R.drawable.alc15,
            R.drawable.alc14,  R.drawable.alc16, R.drawable.alc17, R.drawable.alc18,
            R.drawable.alc19, R.drawable.alc20, R.drawable.alc21, R.drawable.alc22, R.drawable.alc23,
            R.drawable.alc24, R.drawable.alc25, R.drawable.alc26, R.drawable.alc27,
            R.drawable.alc29, R.drawable.alc30, R.drawable.alc31, R.drawable.alc32, R.drawable.alc33,
            R.drawable.alc34, R.drawable.alc35, R.drawable.alc36, R.drawable.alc37, R.drawable.alc38,
            R.drawable.alc39, R.drawable.alc40, R.drawable.alc41};
    MyGridAdapter adapter;

    String setName = null;

    public AddAlcohole() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_alcohole, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        valTextView = (TextView) getActivity().findViewById(R.id.valTextView);
        procentTextView = (TextView) getActivity().findViewById(R.id.procentTextView2);

        valSeekBar = (DiscreteSeekBar) getActivity().findViewById(R.id.valSeek);
        procentSeek = (DiscreteSeekBar) getActivity().findViewById(R.id.procentSeek);

        editName = (EditText) getActivity().findViewById(R.id.inputNameEditText);

        gridOfImages = (GridView) getActivity().findViewById(R.id.gridOfImages);
        gridOfImages.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        addNewAlc = (Button) getActivity().findViewById(R.id.buttonAddNewAlc);

        createGridView();
        setListeners();
    }

    private void createGridView() {
        adapter = new MyGridAdapter(getActivity().getBaseContext(),
                R.layout.griditem, list);

        gridOfImages.setAdapter(adapter);

        gridOfImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setImagePath = i;
            }
        });
    }

    private void setListeners(){
        valSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                volume = valSeekBar.getProgress()*50;
                valTextView.setText((String.valueOf(volume) + " мл"));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        procentSeek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                procent = procentSeek.getProgress()*2;
                procentTextView.setText(String.format("%d %%", procent));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        addNewAlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAlc();
            }
        });
    }

    private void AddAlc()
    {
        if (String.valueOf(editName.getText()).length() != 0){
            Alcohol alcohol = new Alcohol(String.valueOf(editName.getText()), volume, procent, list[setImagePath], 0);
            new AsyncAddAlc().execute(alcohol);
        }else{
            Toast toast = Toast.makeText(getActivity().getBaseContext(),
                    "Введите корректное название", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    class  AsyncAddAlc extends AsyncTask<Alcohol, Void, Void>{
        SQLiteDatabase db;

        @Override
        protected Void doInBackground(Alcohol... alcohols) {
            db=AlcoholDatabaseHelper
                    .openWritableDb(getActivity().getBaseContext());
            AlcoholDatabaseHelper.insertDrink(db, alcohols[0].getName(), alcohols[0].getVolume(),
                    alcohols[0].getVolumeOfAlc(), alcohols[0].getImagePath(), alcohols[0].getFavorite());
                publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast toast = Toast.makeText(getActivity().getBaseContext(),
                    "Напиток создан успешно", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
