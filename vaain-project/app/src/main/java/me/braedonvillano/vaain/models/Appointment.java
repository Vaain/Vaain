package me.braedonvillano.vaain.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Appointment")
public class Appointment extends ParseObject {
    public Appointment() {
    }

    final private static String KEY_BEAUT = "beaut";
    final private static String KEY_PRODUCT = "product";
    final private static String KEY_DATE_TIME = "date_time";
    final private static String KEY_CLIENT = "client";
    final private static String KEY_DESCRIPTION = "description";
    final private static String KEY_SEAT = "seat";
    final private static String KEY_LENGTH = "length";

    public Date getDateTime(){
        return getDate(KEY_DATE_TIME);
    }

    public void setDateTime(Date dateTime) {
        put(KEY_DATE_TIME, dateTime);
    }

    public ParseUser getBeaut(){
        return getParseUser(KEY_BEAUT);
    }

    public void setBeaut(ParseUser beaut) {
        put(KEY_BEAUT, beaut);
    }

    public Number getSeat() {
        return getNumber(KEY_SEAT);
    }

    public void setSeat(Number seat) {
        put(KEY_SEAT, seat);
    }

    public Number getLength() {
        return getNumber(KEY_LENGTH);
    }

    public void setLength(Number length) {
        put(KEY_LENGTH, length);
    }

    public ParseUser getClient(){
        return getParseUser(KEY_CLIENT);
    }

    public void setClient(ParseUser client) {
        put(KEY_CLIENT, client);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public Product getProduct() {
        return (Product) get(KEY_PRODUCT);
    }

    public void setProduct(Product product) {
        put(KEY_PRODUCT, product);
    }

    public static class Query extends ParseQuery<Appointment> {
        public Query() {
            super(Appointment.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withBeaut() {
            include(KEY_BEAUT);
            return this;
        }

        public Query withClient(){
            include(KEY_CLIENT);
            return this;
        }
    }
}
