package com.technawabs.pocketbank.network;

import android.util.Log;

import com.technawabs.pocketbank.Factory;
import com.technawabs.pocketbank.constants.PocketBankConstants;
import com.technawabs.pocketbank.network.exceptions.AccessDeniedException;
import com.technawabs.pocketbank.network.exceptions.ApiStatusException;
import com.technawabs.pocketbank.network.exceptions.HttpException;
import com.technawabs.pocketbank.network.exceptions.ResourceNotFoundException;
import com.technawabs.pocketbank.network.exceptions.ServerErrorException;
import com.technawabs.pocketbank.network.exceptions.UnauthorizedAccessException;
import com.technawabs.pocketbank.network.exceptions.UnsupportedFileTypeException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHandler {

    private static String TAG = "RequestHandler";

    public static String makeRequestAndValidate(Request request) throws JSONException, IOException, HttpException {
        return doRequest(request, true);
    }

    private static String doRequest(Request request, boolean doValidate) throws IOException, HttpException, JSONException {
        Log.d(TAG, request.method() + " : " + request.url());
        OkHttpClient httpClient = Factory.getOkHTTPClient();
        Response response;
        String body = new String();
        response = httpClient.newCall(request).execute();
        if (response == null) {
            return null;
        }
        body = response.body().string();
        Log.d(TAG, response.code() + " : " + body);
        if (!response.isSuccessful()) {
            if (response.code() == 400) {
                throw new HttpException(PocketBankConstants.BAD_REQUEST);
            } else if (response.code() == 401 || response.code() == 1028) {
                throw new AccessDeniedException();
            } else if (response.code() == 404) {
                throw new ResourceNotFoundException(body);
            } else if (response.code() == 403) {
                throw new UnauthorizedAccessException();
            } else if (response.code() == 415) {
                throw new UnsupportedFileTypeException();
            } else if (response.code() >= 500) {
                throw new ServerErrorException();
            } else {
                throw new HttpException(PocketBankConstants.COULDNT_REACH_OUR_SERVERS);
            }
        } else {
            if (doValidate) {
                validateResponse(body);
            }
            return body;
        }
    }

    private static void validateResponse(String body) throws ApiStatusException, JSONException {
        JSONObject response;
        try {
            response = new JSONObject(body);
            if (response.isNull(PocketBankConstants.NotificationConstant.STATUS)) {
                return;
            }
            String status = response.getString(PocketBankConstants.NotificationConstant.STATUS);
            if (!status.equals(PocketBankConstants.NotificationConstant.SUCCESS)) {
                if (response.isNull(PocketBankConstants.NotificationConstant.ERRORS)) {
                    return;
                } else {
                    JSONArray errors = response.getJSONObject(PocketBankConstants.NotificationConstant.ERRORS).getJSONArray(PocketBankConstants.NotificationConstant.ERRS);
                    StringBuilder error = new StringBuilder(2);
                    for (int i = 0; i < errors.length(); i++) {
                        error.append(errors.getJSONObject(i).getString(PocketBankConstants.NotificationConstant.MSG));
                        error.append(". ");
                    }
                    throw new ApiStatusException(error.toString());
                }
            }
        } catch (JSONException ignored) {
            throw new JSONException(body);
        }
    }
}
