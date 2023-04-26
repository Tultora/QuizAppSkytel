package com.skytel.quizapp.Networking;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.skytel.quizapp.Model.PrizeModel;
import com.skytel.quizapp.Model.QuestionsList;
import com.skytel.quizapp.R;
import com.skytel.quizapp.util.JSONParse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.android.volley.Response;

public class FingerRequest {

    private static Context context;
    private static RequestQueue requestQueue = null;

    private static String login = null;
    private static String finger_print = null;
    private static String sendSPS = null;
    private static String sendSign = null;
    private static String sendPdf = null;
    private static String sendUserSign = null;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        FingerRequest.context = context;
    }

    public static void init(Context context) {
        if (requestQueue == null) {
            FingerRequest.context = context;
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.start();
        }
    }

    public static void dispose() {
        if (requestQueue != null) {
            requestQueue.stop();
        }
    }

    public interface FingerRequestEvent<T> {
        public void onSuccess(T result);


        public void onError(SkyRequestError error);
    }

    private static void parseCachedData(String body, Response.Listener<NetworkResponse> listener, FingerRequestEvent event) {
        try {
            JSONObject resultObject = new JSONObject(body);
            NetworkResponse result = new NetworkResponse();
            result.setResult(resultObject);
            listener.onResponse(result);
        } catch (Exception e) {
//            event.onError(SkyRequestError.parseError());
        }
    }

//    public static void getLogin(final FingerRequestEvent<User> event, Context context, final String username, final String password) {
////        if (login == null)
////        login = context.getResources().getString(R.string.url_login);
//        String url = context.getResources().getString(R.string.login);
//        init(context);
//        Response.Listener<NetworkResponse> listener = new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                try {
//                    if (response.getResult() instanceof String) {
//                        System.out.println("???? result 123 login " + response.getResult());
//                        event.onError(SkyRequestError.parseError());
//                        return;
//                    }
//
//                    JSONObject resultObject = (JSONObject) response.getResult();
//                    //"result_code":1000,"result_message":"Амжилттай боллоо","id":"25088","lastname":"Батцэнгэл","fistname":"Мөнгөнтуул","email":"munguntuul@skytel.mn","position":"Систем хөгжүүлэгч",
//                    // "phone":"91100075","vpn":"375",
//                    // "register":"ЕУ97042001","department":"Биллинг, Програм Хөгжүүлэлтийн Алба","division":"Програм хөгжүүлэлтийн хэлтэс \/business\/"
//                    System.out.println("???? resultObject getLogin" + resultObject);
//                    if (resultObject.getInt("result_code") == 1000) {
//                        JSONParse jsonParse = new JSONParse();
//                        event.onSuccess(jsonParse.convertJsonToLogin(resultObject));
//                    } else {
//                        event.onError(new SkyRequestError(resultObject.getInt("result_code"), resultObject.getString("result_message"), 3));
//                    }
//
//                } catch (Exception error) {
//                    event.onError(SkyRequestError.parseError());
//                    System.out.println("???? result 123 error " + error.getMessage());
//
//                }
//            }
//        };
//
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                try {
//                    event.onError(SkyRequestError.parseError());
//                    System.out.println("???? result 123 error VolleyError" + error.getMessage());
//
//                } catch (Exception exception) {
//                    event.onError(SkyRequestError.parseError());
//                    System.out.println("???? result 123 error exception" + exception.getMessage());
//
//                }
//            }
//        };
//
//        String requestBody = "?username=" + username + "&password=" + password + "&token=" + "33546asdfsd378978trtqwe35ds4fg";
//        HashMap<String, String> bodyParams = new HashMap<>();
//        bodyParams.put("username", username + "");
//        bodyParams.put("password", password + "");
//        bodyParams.put("token", "33546asdfsd378978trtqwe35ds4fg");
//
//        NetworkRequest request = new NetworkRequest(url, requestBody, listener, errorListener, bodyParams);
//
//        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        request.setShouldCache(false);
//        requestQueue.add(request);
//
////        NetworkRequest request = new NetworkRequest(Request.Method.POST, url, null, listener, errorListener);
////
////        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10,
////                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////        request.setShouldCache(false);
////        request.addParameter("username", username);
////        request.addParameter("password", password);
////        requestQueue.add(request);
//        System.out.println("request = " + request);
//    }

    public static void getQuestions(final FingerRequestEvent<List<QuestionsList>> event, final Context context, int type) {
        String url = "https://www.skytel.mn/ict/quiz/get/question";
        init(context);
        Response.Listener<NetworkResponse> listener = new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    if (response.getResult() instanceof String) {
                        event.onError(SkyRequestError.parseError());
                        return;
                    }
                    JSONObject resultObject = (JSONObject) response.getResult();
                    System.out.println("getQuestions = " + resultObject);
                    JSONParse parse = new JSONParse();
                    if (resultObject.getJSONArray("questions").length() == 0) {
                        event.onError(new SkyRequestError(1001, "Асуулт байхгүй байна", 3));
                    } else {
                        ArrayList<QuestionsList> questionsLists = new ArrayList<>();
                        JSONArray jsonArray = resultObject.getJSONArray("questions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            questionsLists.add(parse.convertJsonToQuestions(jsonArray.getJSONObject(i)));
                        }
                        event.onSuccess(questionsLists);
                    }
                } catch (Exception error) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    event.onError(SkyRequestError.parseError());
                } catch (Exception exception) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };

        NetworkRequest request = new NetworkRequest(Request.Method.POST, url, null, listener, errorListener);
        request.setShouldCache(false);
        request.setRetryPolicy(new

                DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.addParameter("type", type + "");
        requestQueue.add(request);
    }

    public static void getPrize(final FingerRequestEvent<PrizeModel> event, final Context context, int point, String phone) {
        String url = "https://www.skytel.mn/ict/quiz/grant/prize";
        init(context);
        Response.Listener<NetworkResponse> listener = new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    if (response.getResult() instanceof String) {
                        event.onError(SkyRequestError.parseError());
                        return;
                    }
                    JSONObject resultObject = (JSONObject) response.getResult();
                    System.out.println("getPrize = " + resultObject);

                    if (resultObject.getInt("error_code") == 1000) {
                        JSONParse jsonParse = new JSONParse();
                        event.onSuccess(jsonParse.convertJsonToPrize(resultObject));
                    } else {
                        event.onError(new SkyRequestError(resultObject.getInt("error_code"), resultObject.getString("message"), 3));
                    }
                } catch (Exception error) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    event.onError(SkyRequestError.parseError());
                } catch (Exception exception) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };

        NetworkRequest request = new NetworkRequest(Request.Method.POST, url, null, listener, errorListener);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.addParameter("point", point + "");
        request.addParameter("phone", phone);
        requestQueue.add(request);
    }

    public static void getTanCode(final FingerRequestEvent<String> event, final Context context, String phone) {
        String url = "https://www.skytel.mn/ict/quiz/check/phone";
        //https://www.skytel.mn/ict/quiz/check/phone?phone=91100075
        init(context);
        Response.Listener<NetworkResponse> listener = new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    if (response.getResult() instanceof String) {
                        event.onError(SkyRequestError.parseError());
                        return;
                    }
                    JSONObject resultObject = (JSONObject) response.getResult();
                    System.out.println("getPrize = " + resultObject);

                    if (resultObject.getInt("error_code") == 1000) {
                        JSONParse jsonParse = new JSONParse();
                        event.onSuccess(resultObject.getString("message"));
                    } else {
                        event.onError(new SkyRequestError(resultObject.getInt("error_code"), resultObject.getString("message"), 3));
                    }
                } catch (Exception error) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    event.onError(SkyRequestError.parseError());
                } catch (Exception exception) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };

        NetworkRequest request = new NetworkRequest(Request.Method.POST, url, null, listener, errorListener);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.addParameter("phone", phone);
        requestQueue.add(request);
    }

    public static void checkTanCode(final FingerRequestEvent<String> event, final Context context, String phone, String tanCode) {
        String url = "https://www.skytel.mn/ict/quiz/check/tan";
//https://www.skytel.mn/ict/quiz/check/tan?phone=91100075&tan=323012
        init(context);
        Response.Listener<NetworkResponse> listener = new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    if (response.getResult() instanceof String) {
                        event.onError(SkyRequestError.parseError());
                        return;
                    }
                    JSONObject resultObject = (JSONObject) response.getResult();
                    System.out.println("getPrize = " + resultObject);

                    if (resultObject.getInt("error_code") == 1000) {
                        JSONParse jsonParse = new JSONParse();
                        event.onSuccess(resultObject.getString("message"));
                    } else {
                        event.onError(new SkyRequestError(resultObject.getInt("error_code"), resultObject.getString("message"), 3));
                    }
                } catch (Exception error) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    event.onError(SkyRequestError.parseError());
                } catch (Exception exception) {
                    event.onError(SkyRequestError.parseError());
                }
            }
        };

        NetworkRequest request = new NetworkRequest(Request.Method.POST, url, null, listener, errorListener);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.addParameter("phone", phone);
        request.addParameter("tan", tanCode);
        requestQueue.add(request);
    }
}
