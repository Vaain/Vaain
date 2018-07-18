package me.braedonvillano.vaain;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("vaain-beauty")
                .clientKey("vaain-beauty")
                .server("http://vaain.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
