package com.deco2800.game.components;

import java.util.TimerTask;

public class Timer extends TimerTask {
    private String hour;
    private String minute;
    private String second;
    private int count_Hour;
    private int count_Minute;
    private int count_Second;

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public int getCount_Hour() {
        return count_Hour;
    }

    public void setCount_Hour(int count_Hour) {
        this.count_Hour = count_Hour;
    }

    public int getCount_Minute() {
        return count_Minute;
    }

    public void setCount_Minute(int count_Minute) {
        this.count_Minute = count_Minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getCount_Second() {
        return count_Second;
    }

    public void setCount_Second(int count_Second) {
        this.count_Second = count_Second;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
    public void adjust() {
        /*This function is used to format timer output*/
        if(this.getCount_Hour() < 10)  {
            hour = "0" + this.getCount_Hour();
        } else {
            hour = String.valueOf(this.getCount_Hour());
        }
        if(this.getCount_Minute() < 10)  {
            minute = "0" + this.getCount_Minute();
        } else {
            minute = String.valueOf(this.getCount_Minute());
        }
        if(this.getCount_Second() < 10)  {
            second = "0" + this.getCount_Second();
        } else {
            second = String.valueOf(this.getCount_Second());
        }
    }
    @Override
    public void run() {
        try {
            int total_Time = this.getCount_Hour() * 60 * 60 +
                    this.getCount_Minute() * 60 + this.getCount_Second();
            this.adjust();
            System.out.println(this.getHour() + ":" + this.getMinute() +
                    ":" + this.getSecond());
            for(int i = 0; i < total_Time; total_Time--) {
                if (this.getCount_Second() > 0) {
                    count_Second --;
                } else {
                    if (this.getCount_Minute() > 0) {
                        count_Minute --;
                        this.setCount_Second(59);
                    } else if(this.getCount_Hour() > 0) {
                        count_Hour --;
                        this.setCount_Minute(59);
                    }
                }
                Thread.sleep(1000, 0);
                this.adjust();
                System.out.println(this.getHour() + ":" + this.getMinute() +
                        ":" + this.getSecond());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
