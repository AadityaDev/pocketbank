package com.technawabs.pocketbank.network.exceptions;

import com.technawabs.pocketbank.constants.PocketBankConstants;

public class AccessDeniedException extends HttpException {
    public AccessDeniedException() {
        super(PocketBankConstants.ACCESS_DENIED);
    }
}
