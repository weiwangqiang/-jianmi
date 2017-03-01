package com.example.control;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wangqiang on 2017/1/1.
 */

public class observer implements Observer {
    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {

    }
}
