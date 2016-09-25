package com.technawabs.pocketbank.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.NetbankingOption;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.PaymentResponse;
import com.technawabs.pocketbank.R;
import com.technawabs.pocketbank.Utility;
import com.technawabs.pocketbank.models.ConnectionDto;
import com.technawabs.pocketbank.ui.adapter.ContactUserAdapter;
import com.technawabs.pocketbank.ui.cloudchip.ChipCloud;
import com.technawabs.pocketbank.ui.cloudchip.ChipListener;
import com.technawabs.pocketbank.ui.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import static com.technawabs.pocketbank.constants.PocketBankConstants.rupees;

public class OTPActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ErrorDialog errorDialog;
    private CharSequence Titles[] = {"PHONE", "TWITTER", "GOOGLE"};
    private FragmentTabHost fragmentTabHost;
    private ProgressDialog progressDialog;
    private List<ConnectionDto> connectionDtos;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ContactUserAdapter contactUserAdapter;
    private ChipCloud chipCloud;
    private boolean isRupeesSelected;
    private String message;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

//        fragmentTabHost=(FragmentTabHost)findViewById(R.id.tabhost);
//        fragmentTabHost.setup(OTPActivity.this,getSupportFragmentManager(),R.id.realtabcontent);
//        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[0].toString()).setIndicator(Titles[0]),ContactBookFragment.class,null);
//        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[1].toString()).setIndicator(Titles[1]),TwitterContactFragment.class,null);
//        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[2].toString()).setIndicator(Titles[2]),GoogleContactFragment.class,null);

        chipCloud = (ChipCloud) findViewById(R.id.money_selector);
        new ChipCloud.Configure()
                .chipCloud(chipCloud)
                .selectedColor(Color.parseColor("#F05151"))
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(500)
                .deselectTransitionMS(250)
                .labels(rupees)
                .mode(ChipCloud.Mode.SINGLE)
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        chipCloud.setSelected(true);
                    }

                    @Override
                    public void chipDeselected(int index) {
                        chipCloud.setSelected(false);
                    }
                })
                .build();
        chipCloud.setSelectedChip(1);
        recyclerView = (RecyclerView) findViewById(R.id.contact_list);
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        connectionDtos = new ArrayList<>();
        contactUserAdapter = new ContactUserAdapter(connectionDtos, this, getCitrusClient(), chipCloud, TAG, isRupeesSelected);
        Utility.readContacts(this, TAG, progressDialog, connectionDtos, contactUserAdapter);
        recyclerView.setAdapter(contactUserAdapter);
//        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                for (int i = 0; i < rupees.length; i++) {
//                    if (chipCloud.isSelected(i)) {
//                        isRupeesSelected = true;
//                        money = rupees[i];
//                        Log.d(TAG, chipCloud.isSelected(i) + "");
//                    }
//                }
//                if (isRupeesSelected) {
//                    if (!TextUtils.isEmpty(connectionDtos.get(position).getMobileNo())) {
//                        if (connectionDtos.get(position) != null) {
//                            if (!TextUtils.isEmpty(connectionDtos.get(position).getName())) {
//                                message = money + " sent to " + connectionDtos.get(position).getName();
//
//                            } else {
//                                message = money + " sent to " + connectionDtos.get(position).getMobileNo();
//                            }
//                        }
//                        sendMoney(money, connectionDtos.get(position).getMobileNo(), message);
//                    } else {
//                        Toast.makeText(OTPActivity.this, "Number not found", Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(OTPActivity.this, "Select money amount to be sent", Toast.LENGTH_SHORT).show();
//                }
////                sendMoney();
//            }
//        });

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
//        Amount amount = new Amount("5000");
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
    }

    private void sendMoney(@NonNull final String amount, @NonNull String mobileNumber, @NonNull String messsage) {
        //SendMoney
        getCitrusClient().sendMoneyToMoblieNo(new Amount(amount), mobileNumber, messsage, new Callback<PaymentResponse>() {
            @Override
            public void success(PaymentResponse paymentResponse) {
                Log.d(TAG, paymentResponse.toString());
                Toast.makeText(OTPActivity.this, amount + " is successfully sent! ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(CitrusError error) {
                Log.d(TAG, error.toString());
                Toast.makeText(OTPActivity.this, amount + " is not sent! ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
