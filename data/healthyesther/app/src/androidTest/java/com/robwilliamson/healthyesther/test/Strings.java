package com.robwilliamson.healthyesther.test;

import com.robwilliamson.healthyesther.App;

public class Strings {
    static public String from(int resource) {
        return App.getInstance().getResources().getString(resource);
    }
}
