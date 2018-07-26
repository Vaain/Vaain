package me.braedonvillano.vaain.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Product")
public class BeautSchedule extends ParseObject {
    private static final String KEY_SUN = "sun";
    private static final String KEY_MON = "mon";
    private static final String KEY_TUES = "tues";
    private static final String KEY_WED = "wed";
    private static final String KEY_THURS = "thurs";
    private static final String KEY_FRI = "fri";
    private static final String KEY_SAT = "sat";
    private static final String KEY_BEAUT = "beaut";

    public List<String>  getSunSchedule() {
        return (List<String>) get(KEY_SUN);
    }
    public List<String> getMonSchedule() {
        return (List<String>) get(KEY_MON);
    }
    public List<String> getTuesSchedule() {
        return (List<String>) get(KEY_TUES);
    }
    public List<String> getWedSchedule() {
        return (List<String>) get(KEY_WED);
    }
    public List<String> getThursSchedule() {
        return (List<String>) get(KEY_THURS);
    }
    public List<String> getFriSchedule() {
        return (List<String>) get(KEY_FRI);
    }
    public List<String> getSatSchedule() {
        return (List<String>) get(KEY_SAT);
    }
    public void setSun(List<String> sunSched){
        put(KEY_SUN,sunSched);
    }
    public void setMon(List<String> monSched){
        put(KEY_MON,monSched);
    }
    public void setTues(List<String> tuesSched){
        put(KEY_TUES,tuesSched);
    }
    public void setWed(List<String> wedSched){
        put(KEY_WED,wedSched);
    }
    public void setThurs(List<String> thursSched){
        put(KEY_THURS,thursSched);
    }
    public void setFri(List<String> friSched){
        put(KEY_FRI,friSched);
    }
    public void setSat(List<String> satSched){
        put(KEY_SAT,satSched);
    }


    public void setBeaut(ParseUser user) {
        put(KEY_BEAUT, user);
    }

//    public ParseUser getUser() {
//        return getParseUser(KEY_USER);
//    }
//
//    public void setUser(ParseUser user) {
//        put(KEY_USER, user);
//    }

    public static class Query extends ParseQuery<BeautSchedule> {
        public Query() {
            super(BeautSchedule.class);
            getQuery("Request");
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withBeaut() {
            include(KEY_BEAUT);
            return this;
        }
    }
}
