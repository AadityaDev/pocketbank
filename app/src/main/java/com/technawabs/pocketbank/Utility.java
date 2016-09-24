package com.technawabs.pocketbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.LinkUserExtendedResponse;
import com.citrus.sdk.classes.LinkUserPasswordType;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.technawabs.pocketbank.activities.OTPActivity;
import com.technawabs.pocketbank.ui.dialog.ErrorDialog;

public class Utility {

    public static void showProgressDialog(@NonNull ProgressDialog progressDialog) {
        try {
            if (progressDialog != null) {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    public static void hideProgressDialog(@NonNull ProgressDialog progressDialog) {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public static void citrusLogIn(@NonNull CitrusClient citrusClient, @NonNull final Context context, @NonNull final String tag,
                                   @NonNull final ErrorDialog errorDialog,
                                   @NonNull LinkUserExtendedResponse linkUserExtendedResponse,
                                   @NonNull LinkUserPasswordType linkUserPasswordType,
                                   @NonNull String linkUserPassword) {
        citrusClient.linkUserExtendedSignIn(linkUserExtendedResponse, linkUserPasswordType, linkUserPassword, new Callback<CitrusResponse>() {
            @Override
            public void success(CitrusResponse citrusResponse) {
                try {
                    Log.d(tag, citrusResponse.getMessage());
                    Intent intent=new Intent(context, OTPActivity.class);
                    context.startActivity(intent);
                } catch (Exception e) {
                    errorDialog.show();
                }
            }

            @Override
            public void error(CitrusError error) {
                try {
                    Log.d(tag, error.getMessage());
                    errorDialog.show();
                } catch (Exception e) {
                    errorDialog.show();
                }
            }
        });
    }
}
