package com.sim.tunisierando.Services.Interfaces;

import com.sim.tunisierando.Entities.Comment;

/**
 * Created by Aymen on 04/12/2017.
 */

public interface CommentInterface extends IService<Comment,Integer> {
    public void GetCommentByEventID(VolleyCallback callback,int eventid);
}
