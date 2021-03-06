package com.technawabs.pocketbank;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.LinkUserExtendedResponse;
import com.citrus.sdk.classes.LinkUserPasswordType;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citrus.sdk.response.PaymentResponse;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.technawabs.pocketbank.activities.OTPActivity;
import com.technawabs.pocketbank.constants.PocketBankConstants;
import com.technawabs.pocketbank.models.ConnectionDto;
import com.technawabs.pocketbank.ui.adapter.ContactUserAdapter;
import com.technawabs.pocketbank.ui.cloudchip.ChipCloud;
import com.technawabs.pocketbank.ui.dialog.ErrorDialog;

import java.util.List;

import static com.technawabs.pocketbank.constants.PocketBankConstants.rupees;

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
                                   @NonNull String linkUserPassword,@NonNull final ProgressDialog progressDialog) {
        citrusClient.linkUserExtendedSignIn(linkUserExtendedResponse, linkUserPasswordType, linkUserPassword, new Callback<CitrusResponse>() {
            @Override
            public void success(CitrusResponse citrusResponse) {
                try {
                    Log.d(tag, citrusResponse.getMessage());
                    Utility.hideProgressDialog(progressDialog);
                    Intent intent = new Intent(context, OTPActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                } catch (Exception e) {
                    Utility.hideProgressDialog(progressDialog);
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

    private static String getValidMobileNumber(@NonNull String mobileNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneUtil.parse(mobileNumber, "IN");
        } catch (NumberParseException e) {
//            throw new ProfileStoreException(ProfileStoreExceptionEnum.INVALID_MOBILE_NUMBER);
        }
        return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }


    public static List<ConnectionDto> readContacts(@NonNull Activity context, @NonNull String TAG, @NonNull ProgressDialog progressDialog,
                                                   @NonNull List<ConnectionDto> phoneContactList, @NonNull ContactUserAdapter contactUserAdapter) {
//        List<ConnectionDto> phoneContactList = new ArrayList<>();
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CONTACTS}, 0);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            ContentResolver cr = context.getContentResolver();
            try {
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phone = new String();
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            // get the phone number
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                phone = pCur.getString(
                                        pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            pCur.close();
                            ConnectionDto candidateDto = new ConnectionDto();       // creating an instance for contactmodel
                            candidateDto.setName(name);
                            candidateDto.setMobileNo(phone);
                            // get email and type
                            Cursor emailCur = cr.query(
                                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (emailCur.moveToNext()) {
                                // This would allow you get several email addresses
                                // if the email addresses were stored in an array
                                String email = emailCur.getString(
                                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                String emailType = emailCur.getString(
                                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                                candidateDto.setEmail(email);
                            }
                            if (!TextUtils.isEmpty(candidateDto.getMobileNo()) && candidateDto.getMobileNo().length() > PocketBankConstants.MOBILE_NUMBER_LENGTH) {
                                phoneContactList.add(candidateDto);
                            }
                            emailCur.close();
                        }
                    }
                }
                cur.close();
                contactUserAdapter.notifyDataSetChanged();
                Utility.hideProgressDialog(progressDialog);
            } catch (Exception ex) {
                Log.d(TAG, "Easting up the contacts sync exception as its running in background... ");
                Utility.hideProgressDialog(progressDialog);
            }finally {
                hideProgressDialog(progressDialog);
            }
        }
        return phoneContactList;
    }

    public static int getCircleBackgroundColor(@NonNull int position) {
        if (position % 10 == 0) {
            return R.color.circle10;
        } else if (position % 9 == 0) {
            return R.color.circle9;
        } else if (position % 8 == 0) {
            return R.color.circle8;
        } else if (position % 7 == 0) {
            return R.color.circle7;
        } else if (position % 6 == 0) {
            return R.color.circle6;
        } else if (position % 5 == 0) {
            return R.color.circle5;
        } else if (position % 4 == 0) {
            return R.color.circle4;
        } else if (position % 3 == 0) {
            return R.color.circle3;
        } else if (position % 2 == 0) {
            return R.color.circle2;
        } else {
            return R.color.circle1;
        }
    }

    public static void sendMoneyToContact(@NonNull CitrusClient citrusClient, @NonNull Context context, @NonNull ConnectionDto connectionDto,
                                          @NonNull ChipCloud chipCloud, @NonNull String TAG,
                                          @NonNull boolean isRupeesSelected,
                                          @NonNull ProgressDialog progressDialog) {
        String money = " ", message = " ";
        for (int i = 0; i < rupees.length; i++) {
            if (chipCloud.isSelected(i)) {
                isRupeesSelected = true;
                money = rupees[i];
                Log.d(TAG, chipCloud.isSelected(i) + "");
                break;
            }
        }
        if (isRupeesSelected) {
            if (!TextUtils.isEmpty(connectionDto.getMobileNo())) {
                if (connectionDto != null) {
                    if (!TextUtils.isEmpty(connectionDto.getName())) {
                        message = money + " sent to " + connectionDto.getName();

                    } else {
                        message = money + " sent to " + connectionDto.getMobileNo();
                    }
                }
                sendMoney(context, TAG, citrusClient, money, connectionDto.getMobileNo(), message, progressDialog);
            } else {
                hideProgressDialog(progressDialog);
                Toast.makeText(context, "Number not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            hideProgressDialog(progressDialog);
            Toast.makeText(context, "Select money amount to be sent", Toast.LENGTH_SHORT).show();
        }
    }

    private static void sendMoney(@NonNull final Context context, @NonNull final String TAG, @NonNull CitrusClient citrusClient,
                                  @NonNull final String amount, @NonNull String mobileNumber, @NonNull String messsage,
                                  @NonNull final ProgressDialog progressDialog) {
        //SendMoney
        citrusClient.sendMoneyToMoblieNo(new Amount(amount.replace("Rs.", "").replace(" ", "")), mobileNumber, messsage, new Callback<PaymentResponse>() {
            @Override
            public void success(PaymentResponse paymentResponse) {
                Log.d(TAG, paymentResponse.toString());
                hideProgressDialog(progressDialog);
                Toast.makeText(context, amount + " is successfully sent! ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(CitrusError error) {
                Log.d(TAG, error.toString());
                hideProgressDialog(progressDialog);
                Toast.makeText(context, amount + " is not sent! ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
