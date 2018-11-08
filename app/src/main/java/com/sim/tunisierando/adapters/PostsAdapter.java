package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sim.tunisierando.R;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.AccountActivity;
import com.sim.tunisierando.CommentActivity;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.LikesActivity;
import com.sim.tunisierando.UpdatePostActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;



public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {
    private List<Posts> PostsList;
    private Context context;
    Socket socket;
    SharedPreferences preferences;
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profilepic;
        public TextView content;
        public ImageView image;
        public TextView likes;
        public TextView comments;
        public Button like,comment,settings ;
        public TextView date;
        public MyViewHolder(View view) {
            super(view);

            username = (TextView) view.findViewById(R.id.username);

            profilepic= (ImageView) view.findViewById(R.id.profilepic);

            content = (TextView) view.findViewById(R.id.content);

            image= (ImageView) view.findViewById(R.id.imageurl);

            likes = (TextView) view.findViewById(R.id.likes);
            date = (TextView) view.findViewById(R.id.date);
            comments = (TextView) view.findViewById(R.id.comments);
            like = (Button)view.findViewById(R.id.like);
            comment = (Button)view.findViewById(R.id.comen);
            settings = (Button)view.findViewById(R.id.settings);

        }
    }
    public PostsAdapter(List<Posts> PostsList,@NonNull Context context) {
        this.context = context;
        this.PostsList = PostsList;

        preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public int getItemCount() {
        return PostsList.size();
    }
    @Override
    public PostsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_item, parent, false);



        return new PostsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Posts posts = PostsList.get(position);
        holder.username.setText(posts.getUser().getFisrtName()  +" "+posts.getUser().getLastName() );
        holder.content.setText(posts.getContent());
        holder.content.setText(posts.getContent());
        holder.date.setText(posts.getDate());
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(context, LikesActivity.class);
                i1.putExtra("eventid",posts.getId()+"");
                context.startActivity(i1);
            }
        });
        final JSONObject params = new JSONObject();
        try {

            params.put("idpost",posts.getId());
            params.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkliked", params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.getString("response").equals("true")){

                                holder.like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_liked, 0, 0);
                                holder.likes.setText( posts.getLike()+ " j'aime");
                            }else {
                                holder.likes.setText( posts.getLike()+ " j'aime");
                                holder.like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like, 0, 0);
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

        AppSingleton.getInstance(context).addToRequestQueue(getRequest,"rtt");

        holder.comments.setText(posts.getComment()+" commentaires");

        Picasso.with(context).load(ServerConfig.UrlForImageLocation+posts.getUser().getProfilePicUrl()) .placeholder(R.drawable.loader).into(holder.profilepic);
        if(posts.getImagePath().equals("null")){
            android.view.ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.height = 0;
          holder.image.setLayoutParams(layoutParams);
            holder.content.setTextSize(18);

        }else {
            Picasso.with(context).load(ServerConfig.UrlForImageLocation+posts.getImagePath()) .placeholder(R.drawable.loader).into(holder.image);

        }


       holder.profilepic.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent  i = new Intent(context, AccountActivity.class);
               i.putExtra("iduser",posts.getUser().getId());
               context.startActivity(i);
           }
       });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   socket = IO.socket(ServerConfig.UrlForNotificationServer);
                } catch (URISyntaxException e) {
                    e.printStackTrace();

                }
                socket.connect();

                final JSONObject params = new JSONObject();
                try {

                    params.put("postid",posts.getId());
                    params.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.PUT, ServerConfig.UrlForServer+"/hitlike", params,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Log.d("response", response.getString("response")+"");
                                    Log.d("response", response.getString("like")+"");
                                    if(response.getString("response").equals("liked")){
                                        socket.emit("like",response.getString("user")+" aime votre publication",posts.getId());
                                        holder.like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_liked, 0, 0);
                                        holder.likes.setText(response.getString("like")+" j'aime");




                                    }else {

                                        holder.likes.setText(response.getString("like")+" j'aime");
                                        holder.like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like, 0, 0);

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

                AppSingleton.getInstance(context).addToRequestQueue(getRequest,"rtt");


            }

        });
        final JSONObject paramss = new JSONObject();
        try {

            paramss.put("idpost",posts.getId());
            paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkPostuser", paramss,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("response", response.getString("response")+"");

                            if(response.getString("response").equals("true")){

                                holder.settings.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_post_set, 0);

                            }else {

                                holder.settings.setEnabled(false);
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

        AppSingleton.getInstance(context).addToRequestQueue(getRequestt,"rtt");
        holder.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
               // popup.setOnMenuItemClickListener(context);// to implement on click event on items of menu
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modifier :
                                Intent i = new Intent(context, UpdatePostActivity.class);
                                i.putExtra("idpost",posts.getId());
                                context.startActivity(i);
                                break;
                            case R.id.supprimer :
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);


                                builder.setTitle(" ");

                                //Setting message manually and performing action on button click
                                builder.setMessage("Voulez vous supprimé cette publication ?");
                                //This will not allow to close dialogbox until user selects an option
                                builder.setCancelable(false);
                                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/Removepost/"+posts.getId(), null,
                                                new Response.Listener<JSONObject>()
                                                {
                                                    @Override
                                                    public void onResponse(JSONObject response) {


                                                        try {
                                                            if(response.getString("response").equals("true")){
                                                                PostsList.remove(position);
                                                                notifyItemRemoved(position);
                                                                notifyItemRangeChanged(position, PostsList.size());
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

                                        AppSingleton.getInstance(context).addToRequestQueue(getRequest,"tag");
                                        RequestQueue rq = Volley.newRequestQueue(context);
                                        rq.add(getRequest);

                                    }
                                });
                                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button

                                        dialog.cancel();
                                    }
                                });

                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                //alert.setTitle("AlertDialogExample");
                                alert.show();
                                break;
                        }
                        return false;
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_posts, popup.getMenu());
                popup.show();
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , CommentActivity.class);
                intent.putExtra("eventid",posts.getId());
                context.startActivity(intent);

            }
        });
    }





}