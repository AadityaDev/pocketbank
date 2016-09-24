package com.technawabs.pocketbank.network.exceptions;

import android.support.annotation.Nullable;

import com.technawabs.pocketbank.constants.PocketBankConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class ResourceNotFoundException extends HttpException {
    private String mBody;

    public ResourceNotFoundException() {
        super(PocketBankConstants.MESSAGE_RESOURCE_NOT_FOUND);
        mBody = null;
    }

    public ResourceNotFoundException(String body) {
        super(PocketBankConstants.MESSAGE_RESOURCE_NOT_FOUND);
        try {
            JSONObject jsonObject = new JSONObject(body);
            mBody = jsonObject.getString("error");
        } catch (JSONException e) {
            mBody = null;
        }
    }

    @Nullable
    public String getBody() {
        return mBody;
    }
}
