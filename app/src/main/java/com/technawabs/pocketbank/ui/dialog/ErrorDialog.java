package com.technawabs.pocketbank.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technawabs.pocketbank.R;

public class ErrorDialog extends Dialog {

    private ErrorDialog errorDialog;
    private TextView errorMessage;
    private RelativeLayout ok;
    private String message;

    public ErrorDialog(@NonNull Context context, @NonNull String message) {
        super(context);
        this.errorDialog = this;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.error_dialog);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setText(message);
        ok=(RelativeLayout)findViewById(R.id.ok_section);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorDialog.dismiss();
            }
        });
    }
}
