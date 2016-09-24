package com.technawabs.pocketbank.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technawabs.pocketbank.R;
import com.technawabs.pocketbank.models.ConnectionDto;
import com.technawabs.pocketbank.ui.adapter.ContactUserAdapter;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;

public class TwitterContactFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String TAG=this.getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private List<ConnectionDto> connectionDtos;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ContactUserAdapter contactUserAdapter;
    private TwitterLoginButton twitterLoginButton;

    public TwitterContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TwitterContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TwitterContactFragment newInstance(String param1, String param2) {
        TwitterContactFragment fragment = new TwitterContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_twiiter_contact, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.contact_list);
        twitterLoginButton=(TwitterLoginButton)view.findViewById(R.id.twitter_login);
        progressDialog=ProgressDialog.show(getContext(),"","Loading...",true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        connectionDtos=new ArrayList<>();
        contactUserAdapter=new ContactUserAdapter(connectionDtos,getContext());
        recyclerView.setAdapter(contactUserAdapter);
        return view;
    }

}
