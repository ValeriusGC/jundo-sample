package com.gdetotut.samples.jundosample;

import android.app.Activity;
import android.widget.RadioButton;

import com.gdetotut.jundo.UndoCommand;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by valerius on 18.12.17.
 *
 */
public class MainActivityUndoController implements Serializable{

    public transient MainActivity activity;

    public MainActivityUndoController(@NotNull MainActivity activity) {
        this.activity = activity;
    }

    static class RadioCheckCmd extends UndoCommand {

        private final MainActivityUndoController controller;
        private final int activeBtnRes;
        private final int inactiveBtnRes;

        public RadioCheckCmd(@NotNull String text,
                             @NotNull MainActivityUndoController controller,
                             int activeBtnRes,
                             int inactiveBtnRes,
                             UndoCommand parent) {
            super(text, parent);
            this.controller = controller;
            this.activeBtnRes = activeBtnRes;
            this.inactiveBtnRes = inactiveBtnRes;
        }

        @Override
        protected void doRedo() {
            if(controller.activity != null) {
                RadioButton activeBtn = controller.activity.findViewById(activeBtnRes);
                RadioButton inactiveBtn = controller.activity.findViewById(inactiveBtnRes);
                activeBtn.setChecked(true);
                inactiveBtn.setChecked(false);
            }
        }

        @Override
        protected void doUndo() {
            if(controller.activity != null) {
                RadioButton activeBtn = controller.activity.findViewById(activeBtnRes);
                RadioButton inactiveBtn = controller.activity.findViewById(inactiveBtnRes);
                activeBtn.setChecked(false);
                inactiveBtn.setChecked(true);
            }
        }
    }

}
