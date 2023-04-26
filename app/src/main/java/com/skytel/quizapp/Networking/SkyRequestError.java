package com.skytel.quizapp.Networking;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class SkyRequestError extends VolleyError {

    private static final long serialVersionUID = 1L;
    private int errorCode;
    private String errorMessage;
    private int errorLevel;

    public SkyRequestError(int errorCode, String message, int lvl) {
        this.setErrorCode(errorCode);
        setErrorMessage(message);
        setErrorLevel(lvl);
    }
//    public SkyRequestError(int errorCode, String message, int lvl) {
//        this.setErrorCode(errorCode);
//        setErrorMessage(message);
//        setErrorLevel(lvl);
//    }

    public SkyRequestError(NetworkErrorCodes error) {
        this.setErrorCode(error.getValue());
        setErrorMessage(error.getMessage());
        setErrorLevel(error.getErrorLevel());
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorLevel() {
        return errorLevel;
    }

    public void setErrorLevel(int errorLevel) {
        this.errorLevel = errorLevel;
    }

    public static SkyRequestError notLoggedIn() {
        NetworkErrorCodes code = NetworkErrorCodes.NO_LOGGED_USER;
        SkyRequestError err = new SkyRequestError(code.getValue(), code.getMessage(), code.getErrorLevel());
        return err;
    }

    public static SkyRequestError parseError() {
        NetworkErrorCodes code = NetworkErrorCodes.PARSE_ERROR;
        SkyRequestError err = new SkyRequestError(123, "Мэдээг дуудахад алдаа гарлаа", 1);
        return err;
    }

    public static SkyRequestError noInternet() {
        NetworkErrorCodes errorCode = NetworkErrorCodes.NO_INTERNET_CONNECTION;
        SkyRequestError error = new SkyRequestError(errorCode.getValue(), errorCode.getMessage(), errorCode.getErrorLevel());
        return error;
    }

    public static SkyRequestError parseVolleyError(VolleyError error) {
        NetworkErrorCodes responseCode = null;
        if (error instanceof SkyRequestError) {
            return ((SkyRequestError) error);
        }
        if (error instanceof com.android.volley.NoConnectionError) {
            responseCode = NetworkErrorCodes.NO_INTERNET_CONNECTION;
        } else if (error instanceof com.android.volley.NetworkError) {
            responseCode = NetworkErrorCodes.NETWORK_ERROR;
        } else if (error instanceof com.android.volley.ParseError) {
            responseCode = NetworkErrorCodes.PARSE_ERROR;
        } else if (error instanceof com.android.volley.ServerError) {
            String responseBody;
            try {
                responseBody = new String(error.networkResponse.data, "utf-8");
                JSONObject jsonObject = new JSONObject(responseBody);
                int resultCode = jsonObject.getInt("error_code");
                responseCode = NetworkErrorCodes.parseInternalErrorCodes(resultCode);
            } catch (Exception e) {
                responseCode = NetworkErrorCodes.NETWORK_ERROR;
            }
        } else if (error instanceof com.android.volley.TimeoutError) {
            responseCode = NetworkErrorCodes.SERVER_TIMED_OUT_ERROR;
        } else {
            responseCode = NetworkErrorCodes.SERVER_UNKNOWN_ERROR;
        }
        return new SkyRequestError(responseCode.getValue(), responseCode.getMessage(), responseCode.getErrorLevel());
    }

    @Override
    public String toString() {
        return "SkyRequestError{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorLevel=" + errorLevel +
                '}';
    }
}
