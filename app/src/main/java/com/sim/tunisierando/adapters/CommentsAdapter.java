package com.sim.tunisierando.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.sim.tunisierando.R;
import com.squareup.picasso.Picasso;
import com.sim.tunisierando.Configuration.AppSingleton;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Services.Implementation.CommentService;
import com.sim.tunisierando.Services.Interfaces.CommentInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;





public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    private List<Comment> CommentsList;
    private AppCompatActivity context;
    SharedPreferences preferences;

    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView message;
        public ImageView profilepic;
        public Button settings;

        public MyViewHolder(View view) {
            super(view);

            message = (TextView) view.findViewById(R.id.content);
           username = (TextView) view.findViewById(R.id.username);
            profilepic = (ImageView) view.findViewById(R.id.profilepic);
            settings = (Button)view.findViewById(R.id.settingscomment);




        }
    }
    public CommentsAdapter(List<Comment>CommentsList,@NonNull AppCompatActivity context) {
        this.context = context;
        this.CommentsList = CommentsList;
        preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    @Override
    public int getItemCount() {
        return CommentsList.size();
    }
    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_item, parent, false);



        return new CommentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.MyViewHolder holder, final int position) {
        final Comment Comments = CommentsList.get(position);
        holder.message.setText(Comments.getContent());
        holder.username.setText(Comments.getUser().getFisrtName()  +" "+Comments.getUser().getLastName() );


        Picasso.with(context).load(ServerConfig.UrlForImageLocation+Comments.getUser().getProfilePicUrl()) .placeholder(R.mipmap.ic_launcher).into(holder.profilepic);
        final JSONObject paramss = new JSONObject();
        try {

            paramss.put("idcomment",Comments.getId());
            paramss.put("token",preferences.getString(OAUTH_TOKEN, String.valueOf(Context.MODE_PRIVATE)));

        } catch (Exception e) {
            e.printStackTrace();
        };
        JsonObjectRequest getRequestt = new JsonObjectRequest(Request.Method.POST, ServerConfig.UrlForServer+"/checkcommentuser", paramss,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Modifier votre commentaire .. ");

// Set up the input
                                final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                input.setInputType(InputType.TYPE_CLASS_TEXT );
                                input.setMaxLines(5);
                                builder.setView(input);

                                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/GetCommentById/"+Comments.getId(), null,
                                        new Response.Listener<JSONObject>()
                                        {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                try {


                                                    input.setText(response.getString("content"));


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        },
                                        new Response.ErrorListener()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }
                                );

                                getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                AppSingleton.getInstance(context).addToRequestQueue(getRequest,"rr");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Comment comment = new Comment();
                                        comment.setId(Comments.getId());
                                        comment.setContent(input.getText().toString());
                                        CommentInterface commentInterface = new CommentService(context);
                                        commentInterface.Update(comment);
                                        notifyItemChanged(position);
                                        notifyItemRangeChanged(position, CommentsList.size());
                                    }
                                });
                                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                                break;
                            case R.id.supprimer :
                                final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);


                                builder2.setTitle(" ");

                                //Setting message manually and performing action on button click
                                builder2.setMessage("Voulez vous supprimé votre commentaire ?");
                                //This will not allow to close dialogbox until user selects an option
                                builder2.setCancelable(false);
                                builder2.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ServerConfig.UrlForServer+"/RemoveComment/"+Comments.getId(), null,
                                                new Response.Listener<JSONObject>()
                                                {
                                                    @Override
                                                    public void onResponse(JSONObject response) {


                                                        try {
                                                            if(response.getString("response").equals("true")){
                                                                CommentsList.remove(position);
                                                                notifyItemRemoved(position);
                                                                notifyItemRangeChanged(position, CommentsList.size());
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
                                builder2.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button

                                        dialog.cancel();
                                    }
                                });

                                //Creating dialog box
                                AlertDialog alert = builder2.create();
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


    }



}