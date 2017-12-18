package com.gdetotut.samples.jundosample;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gdetotut.jundo.UndoManager;
import com.gdetotut.jundo.UndoStack;

import java.io.IOException;

import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";
//    UndoManager undoManager;
    UndoStack undoStack;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: " + savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: " + outState);
        UndoManager undoManager = new UndoManager("um", 1, undoStack);
        try {
            String s  = UndoManager.serialize(undoManager, false);
            Log.d(TAG, "saved s = " + s);
            outState.putSerializable("um", undoManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(null != savedInstanceState) {
            String s = savedInstanceState.getSerializable("UndoManager").toString();
            Log.d(TAG, "restored s = " + s);
            if(!s.isEmpty()) {
                try {
                    UndoManager undoManager = UndoManager.deserialize(s);
                    undoStack = undoManager.getStack();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else {
            undoStack = new UndoStack("Stack", null);
        }

    }

    //For Button presses (linked via onClick attribute)
    public void HandleClick(View arg0) {
        double inputRate = 0.0;
        double convertedRate = 0.0;
        EditText inputText = (EditText)findViewById(R.id.editRate);
        TextView convertedText = (TextView)findViewById(R.id.textResult);
        try {
            inputRate = Double.parseDouble(inputText.getText().toString())/100.0;
        } catch (Exception ex) {
            inputText.setText("0.0");
        }

        if(((RadioButton)(findViewById(R.id.radioMonthly))).isChecked()) {
            //If converting to monthly
            convertedRate = (pow((1.0 + inputRate),(1.0/12.0)) - 1.0)*100.0;
        } else {
            //Converting to yearly
            convertedRate = (pow((1.0 + inputRate), 12) - 1.0) * 100.0;
        }
        convertedText.setText(String.format("Converted Rate is %1$.4f%%", convertedRate));
    }
}
