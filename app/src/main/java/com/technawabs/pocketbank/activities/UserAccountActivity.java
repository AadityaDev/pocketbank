package com.technawabs.pocketbank.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.response.CitrusError;
import com.technawabs.pocketbank.R;
import com.technawabs.pocketbank.Utility;
import com.technawabs.pocketbank.models.ConnectionDto;
import com.technawabs.pocketbank.ui.adapter.ContactUserAdapter;
import com.technawabs.pocketbank.ui.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

public class UserAccountActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private List<ConnectionDto> connectionDtos;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ContactUserAdapter contactUserAdapter;
    private ErrorDialog errorDialog;
    private TextView totalPaidAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        totalPaidAmount=(TextView)findViewById(R.id.total_paid_amount);
        recyclerView = (RecyclerView) findViewById(R.id.contact_list);
        linearLayoutManager = new LinearLayoutManager(UserAccountActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        connectionDtos = new ArrayList<>();
//        contactUserAdapter=new ContactUserAdapter(connectionDtos,getContext());
        recyclerView.setAdapter(contactUserAdapter);

        //check the amount
        getCitrusClient().getBalance(new com.citrus.sdk.Callback<Amount>() {
            @Override
            public void success(@NonNull Amount amount) {
                totalPaidAmount.setText(amount.getCurrency()+" "+amount.getValue());
                Log.d(TAG, "Amount " + amount);
                Utility.hideProgressDialog(progressDialog);
            }

            @Override
            public void error(@NonNull CitrusError error) {
                Log.d(TAG, "Error" + error);
                Utility.hideProgressDialog(progressDialog);
                errorDialog = new ErrorDialog(UserAccountActivity.this, getResources().getString(R.string.error_dialog_text));
                errorDialog.show();
            }
        });

    }
}
