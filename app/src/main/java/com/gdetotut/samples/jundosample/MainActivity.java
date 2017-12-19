package com.gdetotut.samples.jundosample;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gdetotut.jundo.UndoManager;
import com.gdetotut.jundo.UndoStack;
import com.gdetotut.jundo.UndoWatcher;

import java.io.IOException;
import java.io.Serializable;

import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity implements UndoWatcher {

    static final String TAG = "MainActivity";
    static final String UM_KEY = "undo_manager";
    UndoStack undoStack;

    Button undoBtn;
    Button redoBtn;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        undoStack.setSubscriber(null);
        UndoManager undoManager = new UndoManager(UM_KEY, 1, undoStack);
        Log.d(TAG, "onSaveInstanceState: " + undoManager);
        try {
            String s  = UndoManager.serialize(undoManager, false);
            Log.d(TAG, "saved s = " + s);
            outState.putString(UM_KEY, s);
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        undoBtn = findViewById(R.id.undo_btn);
        redoBtn = findViewById(R.id.redo_btn);
        RadioButton rbMonth = findViewById(R.id.radioMonthly);
        RadioButton rbYear = findViewById(R.id.radioYearly);

        if(null != savedInstanceState) {
            String s = savedInstanceState.getString(UM_KEY);
            Log.d(TAG, "restored s = " + s);
            if(s != null && !s.isEmpty()) {
                try {
                    UndoManager undoManager = UndoManager.deserialize(s, this);
                    undoStack = undoManager.getStack();
                    undoStack.setWatcher(this);
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        }else {
            undoStack = new UndoStack("Stack", null);
            undoStack.setWatcher(this);
        }

        rbMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoStack.push(new MainUndo.RadioCheckCmd("Do monthly",
                        R.id.radioMonthly, R.id.radioYearly, null));
                setUndoState();
            }
        });
        rbYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoStack.push(new MainUndo.RadioCheckCmd("Do yearly",
                        R.id.radioYearly, R.id.radioMonthly, null));
                setUndoState();
            }
        });

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoStack.undo();
                setUndoState();
            }
        });
        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoStack.redo();
                setUndoState();
            }
        });

        setUndoState();

    }

    //For Button presses (linked via onClick attribute)
    public void handleClick(View arg0) {
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

    @Override
    public void indexChanged(int idx) {
        Log.d(TAG, "indexChanged " + idx);
    }

    @Override
    public void cleanChanged(boolean clean) {

    }

    @Override
    public void canUndoChanged(boolean canUndo) {
        Log.d(TAG, "canUndoChanged " + canUndo);
    }

    @Override
    public void canRedoChanged(boolean canRedo) {
        Log.d(TAG, "canRedoChanged " + canRedo);
    }

    @Override
    public void undoTextChanged(String undoText) {
        Log.d(TAG, "undoTextChanged " + undoText);
        Toast.makeText(this, undoText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void redoTextChanged(String redoText) {
        Log.d(TAG, "redoTextChanged " + redoText);
        Toast.makeText(this, redoText, Toast.LENGTH_SHORT).show();
    }

    private void setUndoState() {
        Log.d(TAG, "setUndoState: " + undoStack.canUndo() + ":" + undoStack.canRedo());
        undoBtn.setEnabled(undoStack.canUndo());
        redoBtn.setEnabled(undoStack.canRedo());
    }
}
