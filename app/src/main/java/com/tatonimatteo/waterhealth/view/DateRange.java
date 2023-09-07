package com.tatonimatteo.waterhealth.view;

import java.util.Date;

public class DateRange {

    private Date startDate;
    private Date endDate;

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange() {
    }

    public DateRange(Long startTime, Long endTime) {
        startDate = new Date(startTime);
        endDate = new Date(endTime);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
