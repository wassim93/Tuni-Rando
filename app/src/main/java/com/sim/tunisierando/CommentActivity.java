package com.sim.tunisierando;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sim.tunisierando.Configuration.ServerConfig;
import com.sim.tunisierando.Configuration.SocketListeners;
import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;
import com.sim.tunisierando.Entities.User;
import com.sim.tunisierando.Services.Implementation.CommentService;
import com.sim.tunisierando.Services.Implementation.UserService;
import com.sim.tunisierando.Services.Interfaces.CommentInterface;
import com.sim.tunisierando.Services.Interfaces.UserInterface;
import com.sim.tunisierando.Services.Interfaces.VolleyCallback;
import com.sim.tunisierando.adapters.CommentsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView myRecylerView ;
    private List<Comment> CommentList ;
    private CommentsAdapter commentsAdapter;
     Button AddCommentBtn;
     EditText message;
     CommentInterface commentInterface ;
    String username ;
    User u ;
    SwipeRefreshLayout mSwipeRefreshLayout ;
    public static final String OAUTH_TOKEN= "token";
    public static final String MyPREFERENCES = "MyPrefs";
    UserInterface userService;
    SharedPreferences preferences;
    int eventid;
    Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        preferences =getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SocketListeners socketListeners = new SocketListeners(this);
        try {
            socket = IO.socket(ServerConfig.UrlForNotificationServer);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        socket.connect();
        userService = new UserService(this);
        userService.getUserByAccessToken(preferences.getString(OAUTH_TOKEN, String.valueOf(MODE_PRIVATE)), new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {


                try {
                    username = result.getString("_fisrt_name");
                   u =  new User(Integer.parseInt( result.getString("id")),
                           result.getString("username"),
                           result.getString("_profile_pic_url"),
                           result.getString("_fisrt_name"),
                           result.getString("_last_name"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

            @Override

            public void onSuccessListProduct(List<Product> result) { }

            public void onSuccessListComments(List<Comment> result) {


            }
        });

        AddCommentBtn = (Button)findViewById(R.id.commenter);
        message = (EditText)findViewById(R.id.message);
        final Bundle extras = getIntent().getExtras();
      eventid = (int)extras.getInt("eventid");
        commentInterface = new CommentService(CommentActivity.this);
        CommentList =  new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.commentlist);
        commentsAdapter = new CommentsAdapter(CommentList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        myRecylerView.setAdapter(commentsAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout) ;
        loadData(eventid);

        AddCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts p = new Posts();
                p.setId(eventid);
           Comment c = new Comment(message.getText().toString(),u,p,"");
                commentInterface.Add(c);

                message.setText(" ");
                socket.emit("comment",username + " a commenter votre publication",eventid);
                 CommentList.add(c);
                  commentsAdapter.notifyDataSetChanged();





            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
    }


    void refreshItems() {
        // Load items
        // ...
        loadData(eventid);
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }
    public void loadData(int item){
        CommentList.clear();

        commentInterface.GetCommentByEventID(new VolleyCallback() {
            @Override
            public void onSuccessUser(JSONObject result) {

            }

            @Override
            public void onSuccessListTips(List<TipsAndTricks> result) {

            }

            @Override
            public void onSuccessListPosts(List<Posts> result) {

            }

            @Override
            public void onSuccessListProduct(List<Product> result) {

            }

            @Override
            public void onSuccessListComments(List<Comment> result) {
                CommentList.addAll(result);
                commentsAdapter.notifyDataSetChanged();
            }
        },item);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

}
