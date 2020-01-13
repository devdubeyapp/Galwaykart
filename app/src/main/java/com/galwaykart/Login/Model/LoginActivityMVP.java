package com.galwaykart.Login.Model;

public interface LoginActivityMVP {

    interface View{



    }

    interface Presenter {

        void setView(LoginActivityMVP.View view);

        void loginButtonClicked();

        void getCurrentUser();

    }

    interface Model {

        void createUser(String name, String lastName);

        User getUser();

        void saveUser(User user);



    }

}
