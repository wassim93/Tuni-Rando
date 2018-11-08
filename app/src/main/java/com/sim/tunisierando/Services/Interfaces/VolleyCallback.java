package com.sim.tunisierando.Services.Interfaces;

import com.sim.tunisierando.Entities.Comment;
import com.sim.tunisierando.Entities.Posts;
import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.Entities.TipsAndTricks;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aymen on 16/11/2017.
 */

public interface VolleyCallback {
    void onSuccessUser(JSONObject result);
    void onSuccessListTips(List<TipsAndTricks> result);
    void onSuccessListPosts(List<Posts> result);

    void onSuccessListProduct(List<Product> result);

    void onSuccessListComments(List<Comment> result);


}
