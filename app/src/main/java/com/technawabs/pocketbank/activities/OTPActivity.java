package com.technawabs.pocketbank.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;

import com.citrus.sdk.Callback;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.NetbankingOption;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.PaymentResponse;
import com.technawabs.pocketbank.R;
import com.technawabs.pocketbank.ui.dialog.ErrorDialog;
import com.technawabs.pocketbank.ui.fragments.ContactBookFragment;
import com.technawabs.pocketbank.ui.fragments.GoogleContactFragment;
import com.technawabs.pocketbank.ui.fragments.TwitterContactFragment;

import java.util.ArrayList;

public class OTPActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ErrorDialog errorDialog;
    private CharSequence Titles[] = {"PHONE", "TWITTER", "GOOGLE"};
    private FragmentTabHost fragmentTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        fragmentTabHost=(FragmentTabHost)findViewById(R.id.tabhost);
        fragmentTabHost.setup(OTPActivity.this,getSupportFragmentManager(),R.id.realtabcontent);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[0].toString()).setIndicator(Titles[0]),ContactBookFragment.class,null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[1].toString()).setIndicator(Titles[1]),TwitterContactFragment.class,null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[2].toString()).setIndicator(Titles[2]),GoogleContactFragment.class,null);

        getCitrusClient().isUserSignedIn(new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean) {
                Log.d(TAG, "Success " + aBoolean);
            }

            @Override
            public void error(CitrusError error) {
                Log.d(TAG, "Error");
                errorDialog = new ErrorDialog(OTPActivity.this, getResources().getString(R.string.error_dialog_text));
                errorDialog.show();
            }
        });

        //check the amount
        getCitrusClient().getBalance(new com.citrus.sdk.Callback<Amount>() {
            @Override
            public void success(@NonNull Amount amount) {
                Log.d(TAG, "Amount " + amount);
            }

            @Override
            public void error(@NonNull CitrusError error) {
                Log.d(TAG, "Error" + error);
                errorDialog = new ErrorDialog(OTPActivity.this, getResources().getString(R.string.error_dialog_text));
                errorDialog.show();
            }
        });

        //Load Money
        getCitrusClient().getLoadMoneyPaymentOptions(new Callback<MerchantPaymentOption>() {
            @Override
            public void success(MerchantPaymentOption loadMoneyPaymentOptions) {
                MerchantPaymentOption merchantPaymentOption = loadMoneyPaymentOptions;
                ArrayList<NetbankingOption> mNetbankingOptionsList = merchantPaymentOption.getNetbankingOptionList();//this will give you only bank list
                Log.d(TAG, mNetbankingOptionsList.toString());
            }

            @Override
            public void error(CitrusError error) {

            }
        });

//        DebitCardOption debitCardOption
//                = new DebitCardOption("Card Holder Name", "4111111111111111", "123", Month.getMonth("12"), Year.getYear("18"));
//
//        Amount amount = new Amount("500");
//
//        // Init Load Money PaymentType
//        final String LOAD_MONEY_RETURN_URL="https://sandbox.citruspay.com/rlvmxr9q74";
//
//        try {
//            PaymentType.LoadMoney loadMoney = new PaymentType.LoadMoney(amount, LOAD_MONEY_RETURN_URL, debitCardOption);
//            // Call Load Money
//            getCitrusClient().simpliPay(loadMoney, new Callback<TransactionResponse>() {
//
//                @Override
//                public void success(TransactionResponse transactionResponse) {
//                    Log.d(TAG,transactionResponse.getMessage());
//                }
//
//                @Override
//                public void error(CitrusError error) {
//                    Log.d(TAG,error.toString());
//                }
//            });
//        }catch (Exception e){
//            Log.d(TAG,e.getMessage());
//        }


        //SendMoney
        getCitrusClient().sendMoneyToMoblieNo(new Amount("10"), "8800919577", "My contribution", new Callback<PaymentResponse>() {
            @Override
            public void success(PaymentResponse paymentResponse) {
                Log.d(TAG, paymentResponse.toString());
            }

            @Override
            public void error(CitrusError error) {
                Log.d(TAG, error.toString());
            }
        });
    }
}
