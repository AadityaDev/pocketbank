package com.technawabs.pocketbank.network.exceptions;

import com.technawabs.pocketbank.constants.PocketBankConstants;

public class UnauthorizedAccessException extends HttpException {

    public UnauthorizedAccessException() {
        super(PocketBankConstants.UNAUTHORIZED_ACCESS);
    }
}
