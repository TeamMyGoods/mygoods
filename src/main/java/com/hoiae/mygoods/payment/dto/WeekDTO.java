package com.hoiae.mygoods.payment.dto;

public class WeekDTO {

    private String weekDay;
    private int weekCount;

    public WeekDTO() {
    }

    public WeekDTO(String weekDay, int weekCount) {
        this.weekDay = weekDay;
        this.weekCount = weekCount;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }

    @Override
    public String toString() {
        return "WeekDTO{" +
                "weekDay='" + weekDay + '\'' +
                ", weekCount=" + weekCount +
                '}';
    }
}
