package com.alexis.borovik.sobrietymeter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class SettingFragment extends Fragment {

    TextView weightTextView;
    DiscreteSeekBar weihtSeek;
    int weight = 70, effectAlc = 2;
    RadioGroup chooseEffectRadioGroupe;
    FloatingActionButton floatButton;


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        weightTextView = (TextView) getActivity().findViewById(R.id.weightTextView);
        weihtSeek =(DiscreteSeekBar) getActivity().findViewById(R.id.weightSeek);
        chooseEffectRadioGroupe = (RadioGroup) getActivity().findViewById(R.id.radioGroupe);
        floatButton =(FloatingActionButton) getActivity().findViewById(R.id.floatingActionButton);
        setWeightSeekListener();
        setChooseRadioGroupeListener();
        setButtonLisener();
        setPreferences();
    }

    private void setButtonLisener() {
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences pref = new Preferences(getActivity().getBaseContext());
                pref.setPreferences(weight, effectAlc);
                Toast toast = Toast.makeText(getActivity().getBaseContext(),
                        "Настройки сохранены", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void setChooseRadioGroupeListener() {

        chooseEffectRadioGroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton1:
                        effectAlc = 1;
                        break;
                    case R.id.radioButton2:
                        effectAlc = 2;
                        break;
                    case R.id.radioButton3:
                        effectAlc = 3;
                        break;
                }
            }
        });
    }

    private void setPreferences(){
        Preferences pref = new Preferences(getActivity().getBaseContext());

        weight = pref.getPreferences(Preferences.APP_REFERENCES_WEIGHT);
        weightTextView.setText(String.
                valueOf(weight + " кг"));

        effectAlc = pref.getPreferences(Preferences.APP_REFERENCES_Effect_OF_ALCOHOL);
        ((RadioButton) getActivity().
                findViewById( R.id.radioButton1
                        - 1 + effectAlc )).
                setChecked(true);
        weihtSeek.setProgress(weight);
    }

    private void setWeightSeekListener() {

        weihtSeek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                weight = value;
                weightTextView.setText((String.valueOf(weight) + " кг"));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
    }


}
