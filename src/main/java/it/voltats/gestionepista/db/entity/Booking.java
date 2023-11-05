package it.voltats.gestionepista.db.entity;

import java.util.Date;

public class Booking {
    private int id;
    private int userId;
    private Date startDate;
    private Date endDate;
    private boolean confirmed;

    public Booking(int userId, Date startDate, Date endDate, boolean confirmed) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.confirmed = confirmed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUser(int userId) {
        this.userId = userId;
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

}
