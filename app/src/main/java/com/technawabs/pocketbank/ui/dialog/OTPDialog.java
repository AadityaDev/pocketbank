package com.technawabs.pocketbank.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.LinkUserExtendedResponse;
import com.citrus.sdk.classes.LinkUserPasswordType;
import com.technawabs.pocketbank.R;
import com.technawabs.pocketbank.Utility;
import com.technawabs.pocketbank.activities.OTPActivity;

public class OTPDialog extends Dialog {

    private Context context;
    private OTPDialog otpDialog;
    private CitrusClient citrusClient;
    private EditText editText;
    private CardView verifyOTP;

    private String tag;
    private ErrorDialog errorDialog;
    private LinkUserExtendedResponse linkUserExtendedResponse;
    private LinkUserPasswordType linkUserPasswordType;
    private ProgressDialog progressDialog;

    public OTPDialog(@NonNull Context context, @NonNull CitrusClient citrusClient, @NonNull String tag,
                     @NonNull ErrorDialog errorDialog, @NonNull LinkUserExtendedResponse linkUserExtendedResponse,
                     @NonNull LinkUserPasswordType linkUserPasswordType) {
        super(context);
        this.otpDialog = this;
        this.citrusClient = citrusClient;
        this.context = context;

        this.tag = tag;
        this.errorDialog = errorDialog;
        this.linkUserExtendedResponse = linkUserExtendedResponse;
        this.linkUserPasswordType = linkUserPasswordType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.otp_dialog);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        editText = (EditText) findViewById(R.id.otp_edit);
        verifyOTP = (CardView) findViewById(R.id.verify_otp);
        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=ProgressDialog.show(context, "", "Loading...", true);
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    errorDialog = new ErrorDialog(context, context.getResources().getString(R.string.error_dialog_text));
                    progressDialog.show();
                    Utility.citrusLogIn(citrusClient, context, tag, errorDialog, linkUserExtendedResponse,
                            linkUserPasswordType, editText.getText().toString(),progressDialog);
                }else {
                    Toast.makeText(context,"Enter OTP code",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
