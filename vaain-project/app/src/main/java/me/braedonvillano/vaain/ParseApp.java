package me.braedonvillano.vaain;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.braedonvillano.vaain.models.Appointment;
import me.braedonvillano.vaain.models.Location;
import me.braedonvillano.vaain.models.Product;
import me.braedonvillano.vaain.models.Request;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Product.class);
        ParseObject.registerSubclass(Appointment.class);
//        ParseObject.registerSubclass(BeautSchedule.class);
        ParseObject.registerSubclass(Location.class);
        ParseObject.registerSubclass(Request.class);
        ParseObject.registerSubclass(Appointment.class);
        // ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("vaain-beauty")
                .clientKey("vaain-beauty")
                .server("http://vaain.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
