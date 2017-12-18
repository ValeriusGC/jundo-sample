package com.gdetotut.samples.jundosample;

import com.gdetotut.jundo.UndoCommand;

import org.jetbrains.annotations.NotNull;

/**
 * Created by valerius on 18.12.17.
 */
public class MainUndo {

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

        /**
         * Constructs an UndoCommand object with the given text.
         *
         * @param text   title for command
         * @param parent possible parent
         */
        public RadioCheckCmd(@NotNull String text, UndoCommand parent) {
            super(text, parent);
        }
    }

}
