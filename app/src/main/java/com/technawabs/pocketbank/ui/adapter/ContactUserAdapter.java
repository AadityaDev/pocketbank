package com.technawabs.pocketbank.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citrus.sdk.CitrusClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.technawabs.pocketbank.R;
import com.technawabs.pocketbank.Utility;
import com.technawabs.pocketbank.models.ConnectionDto;
import com.technawabs.pocketbank.ui.cloudchip.ChipCloud;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ContactUserAdapter extends RecyclerView.Adapter<ContactUserAdapter.ContactsViewHolder> {

    private Context context;
    private CitrusClient citrusClient;
    private List<ConnectionDto> contactsUserList;
    private ChipCloud chipCloud;
    private String TAG;
    private boolean isRupeesSelected;

    public ContactUserAdapter(@NonNull List<ConnectionDto> contactsUserList,@NonNull Context context,
                              @NonNull CitrusClient citrusClient,@NonNull ChipCloud chipCloud,@NonNull String TAG,
                              @NonNull boolean isRupeesSelected) {
        this.contactsUserList = contactsUserList;
        this.context = context;
        this.citrusClient=citrusClient;
        this.chipCloud=chipCloud;
        this.TAG=TAG;
        this.isRupeesSelected=isRupeesSelected;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        return new ContactsViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder holder, final int position) {
        final ConnectionDto contactsUser = contactsUserList.get(position);
        if (contactsUser != null) {
            if (contactsUser.getName() == null && contactsUser.getEmail() == null) {
                holder.comUserName.setText(contactsUser.getMobileNo().isEmpty() ? "" : contactsUser.getMobileNo());
            } else {
                holder.comUserName.setText((contactsUser.getName() != null && !contactsUser.getName().isEmpty()) ? contactsUser.getName() : contactsUser.getEmail());
            }
//            Utility.setImageOrText(holder.context,holder.comUserImageText,holder.comUserImage,
//                    contactsUser.getName() != null && !contactsUser.getName().isEmpty() ? contactsUser.getName() : contactsUser.getEmail(),
//                    TextUtils.isEmpty(contactsUser.getGoogleProfilePic()) ? holder.comUserName.getText().toString() : contactsUser.getGoogleProfilePic(),
//                    position);
            final AtomicBoolean loaded = new AtomicBoolean();
            Picasso.with(holder.context).load(TextUtils.isEmpty(contactsUser.getGoogleProfilePic()) ? holder.comUserName.getText().toString() : contactsUser.getGoogleProfilePic())
                    .into(holder.comUserImage,
                            new Callback.EmptyCallback() {
                                @Override
                                public void onSuccess() {
                                    loaded.set(true);
                                    holder.comUserImageText.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {
                                    holder.comUserImageText.setVisibility(View.VISIBLE);
                                    if (contactsUser.getEmail() != null) {
                                        holder.comUserImageText.setText(contactsUser.getEmail().isEmpty() ? "" : String.valueOf(Character.toUpperCase(contactsUser.getEmail().charAt(0))));
                                    } else if (contactsUser.getName() != null) {
                                        holder.comUserImageText.setText(contactsUser.getName().isEmpty() ? "" : String.valueOf(Character.toUpperCase(contactsUser.getName().charAt(0))));
                                    }
                                    GradientDrawable tvBackground = (GradientDrawable) holder.comUserImageText.getBackground();
                                    tvBackground.setColor(ContextCompat.getColor(holder.context, Utility.getCircleBackgroundColor(position + 1)));
                                }
                            });
            if (!loaded.get()) {
                holder.comUserImageText.setVisibility(View.VISIBLE);
                if (contactsUser.getEmail() != null) {
                    holder.comUserImageText.setText(contactsUser.getEmail().isEmpty() ? "" : String.valueOf(Character.toUpperCase(contactsUser.getEmail().charAt(0))));
                } else if (contactsUser.getName() != null) {
                    holder.comUserImageText.setText(contactsUser.getName().isEmpty() ? "" : String.valueOf(Character.toUpperCase(contactsUser.getName().charAt(0))));
                }
                GradientDrawable tvBackground = (GradientDrawable) holder.comUserImageText.getBackground();
                tvBackground.setColor(ContextCompat.getColor(holder.context, Utility.getCircleBackgroundColor(position + 1)));
            }
            if (contactsUser.getEmail() != null) {
                holder.comUserContactDetail.setText(contactsUser.getEmail());
            } else if (contactsUser.getMobileNo() != null) {
                holder.comUserContactDetail.setText(contactsUser.getMobileNo());
            }
            holder.comRefer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog progressDialog=ProgressDialog.show(context, "", "Loading...", true);
                    Utility.showProgressDialog(progressDialog);
                    Utility.sendMoneyToContact(citrusClient,context,contactsUser,chipCloud,TAG,isRupeesSelected,progressDialog);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactsUserList.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        public TextView comUserName;
        public ImageView comUserImage;
        private TextView comUserImageText;
        private RelativeLayout background;
        private TextView comUserContactDetail;
        public Button comRefer;

        public ContactsViewHolder(Context context, View itemView) {
            super(itemView);
            comUserName = (TextView) itemView.findViewById(R.id.referrer_name);
            comUserImage = (ImageView) itemView.findViewById(R.id.referral_image);
            comUserImageText = (TextView) itemView.findViewById(R.id.referral_image_text);
            comRefer = (Button) itemView.findViewById(R.id.refer_contact);
            background = (RelativeLayout) itemView.findViewById(R.id.top_card);
            comUserContactDetail = (TextView) itemView.findViewById(R.id.connection_contact_detail);
            this.context = context;
        }
    }
}
