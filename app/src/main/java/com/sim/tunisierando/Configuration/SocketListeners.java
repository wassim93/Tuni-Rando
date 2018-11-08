package com.sim.tunisierando.Configuration;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sim.tunisierando.R;
import com.sim.tunisierando.CommentActivity;
import com.sim.tunisierando.DetailEventActivity;
import com.sim.tunisierando.Entities.Events;
import com.sim.tunisierando.LikesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Aymen on 25/12/2017.
 */

public class SocketListeners {

    public Socket socket;
    public Activity activity;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String OAUTH_TOKEN="token";
    SharedPreferences preferences;
    public  SocketListeners(final Activity activity){
        preferences = activity.getSharedPreferences(MyPREFERENCES, activity.MODE_PRIVATE);
        this.activity = activity;
        try {
            socket = IO.socket(ServerConfig.UrlForNotificationServer);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        socket.connect();
        socket.on("messagenot", new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];

                        final String evnetid;
                        try {

                          final String  message = data.getString("message");
                            evnetid= data.getString("eventid");
                            Log.d("dzdz",message);
                            final JSONObject paramss = new JSONObject();
                            try {
                                // user_id, comment_id,status
                                paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(activity.MODE_PRIVATE)));
                                paramss.put("idevent", evnetid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkeventuser", paramss,
                                    new Response.Listener<JSONObject>()
                                    {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if(response.getString("response").equals("true")){
                                                    Intent i  =   new Intent(activity, DetailEventActivity.class);




                                                    /////////////////////////////////////////
                                                    final JSONObject paramss = new JSONObject();
                                                    try {
                                                        // user_id, comment_id,status
                                                        paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(activity.MODE_PRIVATE)));
                                                        paramss.put("idevent", evnetid);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/GetEventById/"+evnetid, paramss,
                                                            new Response.Listener<JSONObject>()
                                                            {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {


                                                                        Events e = new Events();
                                                                        e.setId(Integer.parseInt(response.getString("id")));
                                                                        e.setArrive(response.getString("point_arrive"));
                                                                        e.setContact(response.getString("contact"));
                                                                        e.setDate(response.getString("date"));
                                                                        e.setDepart(response.getString("point_depart"));
                                                                        e.setDescription(response.getString("point_depart"));
                                                                        ArrayList<String> arrayImg= new ArrayList<>();
                                                                        JSONArray arr = response.getJSONArray("image");
                                                                        JSONObject ob = null;

                                                                        for(int j =0 ; j<arr.length();j++){
                                                                            ob = arr.getJSONObject(j);
                                                                            arrayImg.add(ob.getString("image"));

                                                                        }
                                                                        e.setImages(arrayImg);
                                                                        e.setNbrPlace(Integer.parseInt(response.getString("_nbr_places")));
                                                                        e.setType(response.getString("type"));
                                                                        e.setTitle(response.getString("title"));
                                                                        e.setPrix(Integer.parseInt(response.getString("prix")));


                                                                            Intent i  =   new Intent(activity, DetailEventActivity.class);
                                                                            i.putExtra("obj",e);
                                                                            createNotification(message,i,Integer.parseInt(evnetid));


                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }



                                                                }
                                                            },
                                                            new Response.ErrorListener()
                                                            {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Log.d("Error.Response", error.toString());

                                                                }
                                                            }
                                                    );

                                                    getRequestt.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                                    AppSingleton.getInstance(activity).addToRequestQueue(getRequestt,"req");

                                                    //////////////////////////////////////////

                                                }else {

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }



                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("Error.Response", error.toString());

                                        }
                                    }
                            );

                            getRequestt.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            AppSingleton.getInstance(activity).addToRequestQueue(getRequestt,"req");




                        } catch (JSONException e) {
                            return;
                        }


                    }
                });

            }
        });
        socket.on("likenot", new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];

                        final String postid;
                        try {

                            final String message = data.getString("message");
                            postid = data.getString("postid");
                            Log.d("dzdz",message);
                            final JSONObject paramss = new JSONObject();
                            try {
                                // user_id, comment_id,status
                                paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(activity.MODE_PRIVATE)));
                                paramss.put("idpost", postid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkPostuser", paramss,
                                    new Response.Listener<JSONObject>()
                                    {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if(response.getString("response").equals("true")){
                                                    Intent i  =   new Intent(activity, LikesActivity.class);
                                                    createNotification(message,i,Integer.parseInt(postid));
                                                }else {

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }



                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("Error.Response", error.toString());

                                        }
                                    }
                            );

                            getRequestt.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            AppSingleton.getInstance(activity).addToRequestQueue(getRequestt,"req");
                        } catch (JSONException e) {
                            return;
                        }


                    }
                });

            }
        });

        socket.on("commenton", new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];

                        final String postid;
                        try {

                            final String message = data.getString("message");
                            postid = data.getString("postid");
                            Log.d("dzdz",message);
                            final JSONObject paramss = new JSONObject();
                            try {
                                // user_id, comment_id,status
                                paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(activity.MODE_PRIVATE)));
                                paramss.put("idpost", postid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkPostuser", paramss,
                                    new Response.Listener<JSONObject>()
                                    {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if(response.getString("response").equals("true")){
                                                    Intent i  =   new Intent(activity, CommentActivity.class);
                                                    createNotification(message,i,Integer.parseInt(postid));
                                                }else {

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }



                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("Error.Response", error.toString());

                                        }
                                    }
                            );

                            getRequestt.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                           AppSingleton.getInstance(activity).addToRequestQueue(getRequestt,"req");
                        } catch (JSONException e) {
                            return;
                        }


                    }
                });

            }
        });
        socket.on("message", new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
               activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];



                        try {
                            String iduser = data.getString("iduser");
                            String message = data.getString("message") ;

                            final JSONObject params1 = new JSONObject();
                            try {

                                params1.put("token", preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));
                                params1.put("iduser",iduser);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            final String finalMessage = message;
                            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checksubscribe", params1,
                                    new Response.Listener<JSONObject>()
                                    {
                                        @Override
                                        public void onResponse(JSONObject response) {


                                            try {
                                                Log.d("hghg",response.getString("response"));
                                                if(response.getString("response").equals("true")){

                                                    createNotification2(finalMessage);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("Error.Response", error.toString());

                                        }
                                    }
                            );

                            getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            AppSingleton.getInstance(activity).addToRequestQueue(getRequest,"tag");
                            RequestQueue rq = Volley.newRequestQueue(activity);




                        } catch (JSONException e) {
                            return;
                        }


                    }
                });

            }
        });
    }
    private final void createNotification(String message,Intent i ,int id){
        final NotificationManager mNotification = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

        Intent intent = i;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("eventid",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Notification.Builder builder = new Notification.Builder(activity)
                .setWhen(System.currentTimeMillis())
                .setTicker("Tuni-Rando")
                .setSmallIcon(R.drawable.bonfire)
                .setContentTitle("Tuni-Rando notification")
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                ;

        mNotification.notify(1, builder.build());
    }
    private  void createNotification2(String message) {
        final NotificationManager mNotification = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(activity)
                .setWhen(System.currentTimeMillis())
                .setTicker("Tuni-Rando")
                .setSmallIcon(R.drawable.bonfire)
                .setContentTitle("Tuni-Rando notification")
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOnlyAlertOnce(true);

        mNotification.notify(1, builder.build());
    }
}
