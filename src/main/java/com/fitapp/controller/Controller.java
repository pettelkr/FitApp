package com.fitapp.controller;

import com.fitapp.navigation.Navigator;

public interface Controller {
    public void changeView(String fxmlFile);
    public void setNavigator(Navigator navigator);
}
