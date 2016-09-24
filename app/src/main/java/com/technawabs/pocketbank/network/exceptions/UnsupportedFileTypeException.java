package com.technawabs.pocketbank.network.exceptions;

import com.technawabs.pocketbank.constants.PocketBankConstants;

public class UnsupportedFileTypeException extends HttpException {

    public UnsupportedFileTypeException() {
        super(PocketBankConstants.MESSAGE_UNSUPPORTED_FILE_TYPE);
    }

}
