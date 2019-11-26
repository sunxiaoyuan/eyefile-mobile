package com.simon.margaret.observer;

/**
 * Created by apple on 2019/4/15.
 * 被观察者接口
 */

public interface SubjectListener {

    void add(ObserverListener observerListener);

    void notifyObserver(Object msg);

    void remove(ObserverListener observerListener);
}
