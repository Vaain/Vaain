package me.braedonvillano.vaain.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Product")
public class Product extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_BEAUT = "beaut";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String description) {
        put(KEY_NAME, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public Number getPrice() {
        return getNumber(KEY_PRICE);
    }

    public void setPrice(Number price) {
        put(KEY_PRICE, price);
    }

    public Date getCreatedAt() {
        return getDate(KEY_CREATED_AT);
    }

    public ParseUser getBeaut() {
        return getParseUser(KEY_BEAUT);
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

    public static class Query extends ParseQuery<Product> {
        public Query() {
            super(Product.class);
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
