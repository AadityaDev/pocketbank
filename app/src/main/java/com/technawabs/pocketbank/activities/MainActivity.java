package com.technawabs.pocketbank.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.LinkUserExtendedResponse;
import com.citrus.sdk.classes.LinkUserPasswordType;
import com.citrus.sdk.classes.LinkUserSignInType;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.technawabs.pocketbank.R;
import com.technawabs.pocketbank.Utility;
import com.technawabs.pocketbank.ui.dialog.ErrorDialog;
import com.technawabs.pocketbank.ui.dialog.OTPDialog;

public class MainActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ErrorDialog errorDialog;
    private Button signUp;
    private EditText userEmail;
    private EditText userMobile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = (Button) findViewById(R.id.sign_up);
        userEmail = (EditText) findViewById(R.id.user_email);
        userMobile = (EditText) findViewById(R.id.user_mobile);
        //Setup citrus client
//        citrusClient = CitrusClient.getInstance(getApplicationContext());
//        citrusClient.enableLog(true);
//        citrusClient.init("rlvmxr9q74-signup", "c3545d28806d28b015768830885fe105",
//                "rlvmxr9q74-signin",
//                "736b3b82775db4b9b557e966ecb4a8a0",
//                "rlvmxr9q74", Environment.SANDBOX);
//
//        citrusClient.isUserSignedIn(new com.citrus.sdk.Callback<Boolean>() {
//            @Override
//            public void success(Boolean loggedIn) {
//                Log.d(TAG, "Success " + loggedIn);
//            }
//
//            @Override
//            public void error(CitrusError error) {
//                Log.d(TAG, "Error" + error.toString());
//            }
//        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(userEmail.getText().toString()) && !TextUtils.isEmpty(userMobile.getText().toString())) {
                    progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...", true);
                    getCitrusClient().linkUserExtended(userEmail.getText().toString(), userMobile.getText().toString(), new Callback<LinkUserExtendedResponse>() {
                        @Override
                        public void success(LinkUserExtendedResponse linkUserExtendedResponse) {
                            // User Linked!
                            Log.d(TAG, linkUserExtendedResponse.toString());
                            //UserSignIn type
                            LinkUserSignInType linkUserSignInType = linkUserExtendedResponse.getLinkUserSignInType();
                            if (linkUserSignInType.toString().equals("None ")) {
                                Utility.hideProgressDialog(progressDialog);
                                Log.d(TAG, "Error occurred");
                            } else {
                                authenticationType(linkUserSignInType);
                                String linkUserMessage = linkUserExtendedResponse.getLinkUserMessage();
                                Log.d(TAG, linkUserMessage);
                                ErrorDialog errorDialog=new ErrorDialog(MainActivity.this,"Error Occurreed");
                                Utility.hideProgressDialog(progressDialog);
                                OTPDialog otpDialog=new OTPDialog(MainActivity.this,getCitrusClient(),TAG,errorDialog,
                                        linkUserExtendedResponse,LinkUserPasswordType.Otp);
                                otpDialog.show();
                            }
                        }

                        @Override
                        public void error(CitrusError error) {
                            // Error case
                            try {
                                String errorMessasge = error.getMessage();
                                Log.d(TAG, errorMessasge);
                                Utility.hideProgressDialog(progressDialog);
                                errorDialog = new ErrorDialog(MainActivity.this, errorMessasge);
                                errorDialog.show();
                            } catch (Exception e) {
                                Utility.hideProgressDialog(progressDialog);
                                errorDialog = new ErrorDialog(MainActivity.this, getResources().getString(R.string.error_dialog_text));
                                errorDialog.show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your email and mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void init()
//    {
//        Config citrus = new Config();
//        citrus.setEnv("sandbox"); //replace it with production when you are ready
//        citrus.setupSignupId("merchant-signup");
//        citrus.setupSignupSecret("3e2288d3a1a3f59ef6f93373884d2ca1");
//        citrus.setSigninId("merchant-wallet");
//        citrus.setSigninSecret("c40798d3c12114b5bb19f2051d9ed181");
//    }

    public void authenticationType(@NonNull LinkUserSignInType linkUserSignInType) {
        switch (linkUserSignInType.ordinal()) {
            case 0:
                Log.d(TAG, "");
                break;
            case 1:
                Log.d(TAG, "SignInTypeMOtpOrPassword ");
                break;
            case 2:
                Log.d(TAG, "SignInTypeMOtp ");
                break;
            case 3:
                Log.d(TAG, "SignInTypeEOtpOrPassword ");
                break;
            case 4:
                Log.d(TAG, "SignInTypeEOtp ");
                break;
            case 5:
                Log.d(TAG, "None ");
                break;
        }
    }

    public void citrusLogIn(@NonNull LinkUserExtendedResponse linkUserExtendedResponse, @NonNull LinkUserPasswordType linkUserPasswordType,
                            @NonNull String linkUserPassword) {
        getCitrusClient().linkUserExtendedSignIn(linkUserExtendedResponse, linkUserPasswordType, linkUserPassword, new Callback<CitrusResponse>() {
            @Override
            public void success(CitrusResponse citrusResponse) {
                try {
                    Log.d(TAG, citrusResponse.getMessage());
                } catch (Exception e) {
                    errorDialog = new ErrorDialog(MainActivity.this, getResources().getString(R.string.error_dialog_text));
                    errorDialog.show();
                }
            }

            @Override
            public void error(CitrusError error) {
                try {
                    Log.d(TAG, error.getMessage());
                    errorDialog = new ErrorDialog(MainActivity.this, error.getMessage());
                } catch (Exception e) {
                    errorDialog = new ErrorDialog(MainActivity.this, getResources().getString(R.string.error_dialog_text));
                    errorDialog.show();
                }


            }
        });
    }

    public void citrusLogInWithOTP(@NonNull String emailId, @NonNull LinkUserExtendedResponse linkUserExtendedResponse, @NonNull LinkUserPasswordType linkUserPasswordType,
                                   @NonNull String linkUserPassword) {
        getCitrusClient().linkUserExtendedSignIn(linkUserExtendedResponse, linkUserPasswordType, linkUserPassword, new Callback<CitrusResponse>() {
            @Override
            public void success(CitrusResponse citrusResponse) {
                Log.d(TAG, citrusResponse.getMessage());
            }

            @Override
            public void error(CitrusError error) {
                Log.d(TAG, error.getMessage());
            }
        });
//        citrusClient.linkUserExtendedVerifyEOTPAndUpdateMobile(emailId,);
    }
}
