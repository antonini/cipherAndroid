package com.tiagomissiato.cipherteste.conn;

/**
 * Created by trigoleto on 12/2/14.
 */

import com.google.gson.annotations.SerializedName;

public class ServerException {

    private static final String TAG = ServerException.class.getName();

    public ServerException() {
        this.result = new GenericResult();
    }

    public Errors error;
    public GenericResult result;
    public int status;

    public class GenericResult {
        public String message;
        public int status;
    }

    public class Errors {
        @SerializedName("code")
        public int code;

        @SerializedName("name")
        public String name;

        @SerializedName("status")
        public String status;

        @SerializedName("message")
        public String message;
    }

    public class ErrorCode {
        public static final int TIMEOUT = 408;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int PAGE_NOT_FOUNDED = 404;
        public static final int BAD_REQUEST = 400;

        public static final int SERVER_GENERIC_CODE = -1;
        public static final int SERVER_DOWNLOAD_ERROR = -99;
        public static final int NULL = -6;
        public static final int Exception = -5;
        public static final int JsonSyntaxException = -4;
        public static final int NO_CONNECTION = -5;
        public static final int INVALID_TOKEN = -10;

    }

    public Errors getErrorByCode(int code){
        Errors error = new Errors();
        error.code = code;
        error.name = getName(code);
        error.message = getName(code);
        return error;
    }

    public String getName(int code){

        switch(code){
            case ErrorCode.TIMEOUT:
                return "TIMEOUT";
            case ErrorCode.INTERNAL_SERVER_ERROR:
                return "INTERNAL_SERVER_ERROR";
            case ErrorCode.PAGE_NOT_FOUNDED:
                return "PAGE_NOT_FOUNDED";
            case ErrorCode.BAD_REQUEST:
                return "BAD_REQUEST";
            case ErrorCode.NO_CONNECTION:
                return "NO_CONNECTION";
            case ErrorCode.INVALID_TOKEN:
                return "INVALID_TOKEN";
            default:
                return String.valueOf(code);

        }

    }

    public String getName(){

        switch(result.status){
            case ErrorCode.TIMEOUT:
                return "TIMEOUT";
            case ErrorCode.INTERNAL_SERVER_ERROR:
                return "INTERNAL_SERVER_ERROR";
            case ErrorCode.PAGE_NOT_FOUNDED:
                return "PAGE_NOT_FOUNDED";
            case ErrorCode.BAD_REQUEST:
                return "BAD_REQUEST";
            case ErrorCode.NO_CONNECTION:
                return "NO_CONNECTION";
            case ErrorCode.INVALID_TOKEN:
                return "INVALID_TOKEN";
            default:
                return String.valueOf(result.status);

        }

    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        if(result != null)
            sb.append("result:status: ").append(result.status).append(";");
        else
            sb.append("status: ").append(status).append(";");

        sb.append("statusCode: ").append(error.code).append(";");
        sb.append("status: ").append(error.name).append(";");
        sb.append("message: ").append(error.message).append(";");

        return  sb.toString();
    }

}