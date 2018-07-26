package me.braedonvillano.vaain.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;

@ParseClassName("Request")
public class Request extends ParseObject{
    public Request() {
    }

    final private static String KEY_BEAUT = "beaut";
    final private static String KEY_PRODUCT = "product";
    final private static String KEY_DATE_TIME = "date_time";
    final private static String KEY_CLIENT = "client";
    final private static String KEY_DESCRIPTION = "description";

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

    public static class Query extends ParseQuery<Request> {
        public Query() {
            super(Request.class);
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
