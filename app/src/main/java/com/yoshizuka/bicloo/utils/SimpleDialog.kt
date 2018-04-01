package com.yoshizuka.bicloo.utils

import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.support.v7.app.AlertDialog
import android.widget.EditText



open class SimpleDialog {
    companion object {

        /**
         * Affiche un simple popup de recherche
         * @param context Le context
         * @param listener Texte retourner à la validation
         *
         */
        fun inputTextDialog(context: Context, listener: (String) -> Unit, title: String = "") {
            val builder = AlertDialog.Builder(context)
            if(!title.isEmpty()) builder.setTitle(title)

            // Set up the input
            val input = EditText(context)
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which -> listener(input.text.toString()) })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }
    }
}