package com.skytel.quizapp.Networking;

import com.skytel.quizapp.R;

public enum  NetworkErrorCodes {

    NO_INTERNET_CONNECTION(100, R.string.no_internet, 2),
    NETWORK_ERROR(101, R.string.server_unknown, 1),
    SERVER_TIMED_OUT_ERROR(102, R.string.server_unknown, 2),
    NO_LOGGED_USER(104, R.string.no_user, 2),
    SERVER_UNKNOWN_ERROR(103, R.string.server_unknown, 2),
    PARSE_ERROR(200, R.string.parse_error, 0),
    AUTHENTICATION_ERROR(1002, R.string.authentication_error, 0),
    YOUTH_HAS_NO_COUPLE(4101, R.string.youth_has_no_couple, 0);

    private int value;
    private int messageId;
    private int errorLevel;

    NetworkErrorCodes(int value, int message, int lvl) {
        this.value = value;
        messageId = message;
        errorLevel = lvl;
    }

    public int getErrorLevel() {
        return errorLevel;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        if (messageId != 0) {
            return FingerRequest.getContext().getString(messageId);
        } else {
            return null;
        }
    }

    public static NetworkErrorCodes parseInternalErrorCodes(int remoteCode) {
        switch (remoteCode) {
            case 1002:
                return AUTHENTICATION_ERROR;
            default:
                return PARSE_ERROR;
        }
    }
}
