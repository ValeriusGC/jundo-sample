package com.gdetotut.samples.jundosample;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.gdetotut.jundo.UndoCommand;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by valerius on 18.12.17.
 */
public class MainUndo implements Serializable{

    static class AddCmd extends UndoCommand {

        /**
         * Constructs an UndoCommand object with the given text.
         *
         * @param text   title for command
         * @param parent possible parent
         */
        public AddCmd(@NotNull String text, UndoCommand parent) {
            super(text, parent);
        }

    }

    static class RadioCheckCmd extends UndoCommand {

        int activeBtnRes;
        int inactiveBtnRes;

        /**
         * Constructs an UndoCommand object with the given text.
         *
         * @param text   title for command
         * @param parent possible parent
         */
        public RadioCheckCmd(@NotNull String text, int activeBtnRes, int inactiveBtnRes, UndoCommand parent) {
            super(text, parent);
            this.activeBtnRes = activeBtnRes;
            this.inactiveBtnRes = inactiveBtnRes;
        }

        @Override
        protected <Context> void doRedo(Context context) {
            if(context != null && context instanceof Activity) {
                Activity a = (Activity)context;
                RadioButton activeBtn = a.findViewById(activeBtnRes);
                RadioButton inactiveBtn = a.findViewById(inactiveBtnRes);
                activeBtn.setChecked(true);
                inactiveBtn.setChecked(false);
            }
        }

        @Override
        protected <Context> void doUndo(Context context) {
            if(context != null && context instanceof Activity) {
                Activity a = (Activity)context;
                RadioButton activeBtn = a.findViewById(activeBtnRes);
                RadioButton inactiveBtn = a.findViewById(inactiveBtnRes);
                inactiveBtn.setChecked(true);
                activeBtn.setChecked(false);
            }
        }

    }

}
