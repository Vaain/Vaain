package me.braedonvillano.vaain.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;


@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_IMAGE = "media";
    private static final String KEY_BEAUT = "beaut";

    public Post() {}

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public Product getProduct() {
        return (Product) get(KEY_PRODUCT);
    }

    public void setProduct(Product product) {
        put(KEY_PRODUCT, product);
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

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withBeaut() {
            include(KEY_BEAUT);
            return this;
        }

        public Query withProduct() {
            include(KEY_PRODUCT);
            return this;
        }
    }
}
