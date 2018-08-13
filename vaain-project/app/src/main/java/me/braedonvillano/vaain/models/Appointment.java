package me.braedonvillano.vaain.models;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@ParseClassName("Appointment")
public class Appointment extends ParseObject{
    public Appointment() {}

    final private static String KEY_BEAUT = "beaut";
    final private static String KEY_PRODUCT = "product";
    final private static String KEY_DATE_TIME = "date_time";
    final private static String KEY_CLIENT = "client";
    final private static String KEY_DESCRIPTION = "description";
    final private static String KEY_SEAT = "seat";
    final private static String KEY_LENGTH = "length";
    final private static String KEY_COMMENT = "comment";
    final private static String KEY_STATUS = "isComplete";
    final private static String KEY_LOC = "location";

    public Date getDateTime(){
        return getDate(KEY_DATE_TIME);
    }

    public String getStrDateTime(){
        Date date = getDateTime();
        DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy h:mm ");
        return dateFormat.format(date) ;
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

    public Location getLocation(){
        return (Location)get("location");
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
        put(KEY_COMMENT, description);
    }

    public Boolean getStatus(){
        return getBoolean(KEY_STATUS);
    }

    public void setStatus(Boolean status){
        put(KEY_STATUS,status);
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

        public Query withClient() {
            include(KEY_CLIENT);
            return this;
        }

        public Query withLocation(){
            include(KEY_LOC);
            return this;
        }

        public Query withProduct() {
            include(KEY_PRODUCT);
            return this;
        }
    }
}

