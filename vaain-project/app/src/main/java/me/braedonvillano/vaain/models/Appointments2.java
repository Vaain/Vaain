package me.braedonvillano.vaain.models;


import com.parse.Parse;
import com.parse.ParseFile;

import org.parceler.Parcel;

@Parcel
public class Appointments2 {

     ParseFile beautImage;
     ParseFile proImage;
     String proName;
     String Date;
     String price;
     String description;
     String beautName;
     String clientName;
     ParseFile clientImage;
     Location location;

    Appointment appt;

    public Appointments2(Appointment newAppt){
       appt =  newAppt;
       beautImage = appt.getBeaut().getParseFile("profileImage");
       proImage = appt.getProduct().getImage();
       beautName = appt.getBeaut().getString("Name");
       Date = appt.getStrDateTime();
       price = appt.getProduct().getPrice().toString();
       description = appt.getDescription();
       proName = appt.getProduct().getName();
       clientName = appt.getClient().getString("Name");
       clientImage = appt.getClient().getParseFile("profileImage");
       location = (Location) appt.getLocation();

    }

    public Appointments2(){}

    public String getClientName() {
        return clientName;
    }

    public ParseFile getClientImage() {
        return clientImage;
    }

    public Location getLocation(){
        return location;
    }

    public ParseFile getProImage() {
        return proImage;
    }

    public String getProName() {
        return proName;
    }

    public String getDate() {
        return Date;
    }

    public String getPrice() {
        return "$ " + price;
    }

    public String getDescription() {
        return description;
    }

    public String getBeautName() {
        return beautName;
    }

    public ParseFile getBeautImage() {

        return beautImage;
    }
}
