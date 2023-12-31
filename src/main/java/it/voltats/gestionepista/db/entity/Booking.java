package it.voltats.gestionepista.db.entity;

import it.voltats.gestionepista.db.entity.model.BookingStatus;

import java.rmi.server.UID;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
    private int id;
    private int userId;
    private Date startDate;
    private Date endDate;
    private BookingStatus status;
    private double price;

    public Booking(int userId, Date startDate, Date endDate, BookingStatus status, double price) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.price = price;
    }

    /* Getters & Setters */
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /* toString */
    @Override
    public String toString() {
        return "ID: "+ id + " - USERID: " + userId + " - DATE: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(startDate)
                + " - " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(endDate) + " - STATUS: " + status.name()
                + " - PRICE: " + price;
    }
}
