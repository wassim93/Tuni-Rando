package com.sim.tunisierando.Services.Interfaces;

import com.sim.tunisierando.Entities.TipsAndTricks;

/**
 * Created by Aymen on 20/11/2017.
 */

public interface TipsAndTricksInterface extends IService<TipsAndTricks,Integer> {
    void GetAll(VolleyCallback callback);
}
