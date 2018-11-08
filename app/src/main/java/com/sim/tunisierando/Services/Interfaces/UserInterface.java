package com.sim.tunisierando.Services.Interfaces;


import com.sim.tunisierando.Entities.User;




public interface UserInterface extends IService<User,Integer>{
void ActivateAccount(String email,String code);
    void  Login(String email,String password);
    void  SetBackgroundImage(User u );
    void  getUserByAccessToken(String Token,final VolleyCallback callback);
    void  Logout();


}
