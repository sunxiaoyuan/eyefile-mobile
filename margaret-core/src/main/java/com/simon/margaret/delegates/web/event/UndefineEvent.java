package com.simon.margaret.delegates.web.event;

import com.simon.margaret.util.log.MargaretLogger;

/**
 * Created by apple on 2019/8/31.
 */

public class UndefineEvent extends Event {

    @Override
    public String execute(String params) {
        MargaretLogger.e("UndefineEvent", params);
        return null;
    }
}

