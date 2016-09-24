package com.technawabs.pocketbank.network.exceptions;

import com.technawabs.pocketbank.constants.PocketBankConstants;

public class ServerErrorException extends HttpException {

    public ServerErrorException() {
        super(PocketBankConstants.MESSAGE_SERVER_ERROR);
    }
}
