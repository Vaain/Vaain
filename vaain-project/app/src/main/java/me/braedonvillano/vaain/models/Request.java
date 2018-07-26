package me.braedonvillano.vaain.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
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


    public Date getDateTime(){
       return getDate(KEY_DATE_TIME);
    }

    public ParseUser getBeaut(){
        return getParseUser(KEY_BEAUT);
    }
    public ParseUser getClient(){
        return getParseUser(KEY_CLIENT);
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
