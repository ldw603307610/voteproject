package com.li.voteproject;

import org.springframework.stereotype.Component;

@Component
public class VoteTime {
    int time = 0;
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
