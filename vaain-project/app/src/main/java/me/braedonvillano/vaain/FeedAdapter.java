package me.braedonvillano.vaain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Post;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    public List<Post> mPosts;
    static Context context;
    private Vibrator myVib;

    public FeedAdapter(List<Post> posts) {
        mPosts = new ArrayList<>();
        mPosts.addAll(posts);
    }

    // clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_post, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder( final ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        ParseUser beaut = post.getBeaut();

        holder.tvPostDescription.setText(post.getDescription());
        holder.tvPostUsername.setText(beaut.getString("Name"));

        holder.ivPostImage.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(context, "Left", Toast.LENGTH_SHORT).show();
                holder.ivPostImage.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(context, "Right", Toast.LENGTH_SHORT).show();
                holder.ivPostImage.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });

        holder.btnViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Start New Activity", Toast.LENGTH_LONG).show();
            }
        });

        if (post.getImage() != null) {
            Glide.with(context)
                    .load(post.getImage().getUrl())
                    .into(holder.ivPostImage);
        }

        if (beaut.getParseFile("profileImage") != null) {
            Glide.with(context)
                    .load(beaut.getParseFile("profileImage").getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivPostProfile);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPostImage;
        public ImageView ivPostProfile;
        public TextView tvPostDescription;
        public TextView tvPostUsername;
        public Button btnViewProduct;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivPostProfile = itemView.findViewById(R.id.ivPostProfile);
            tvPostUsername = itemView.findViewById(R.id.tvPostUsername);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            btnViewProduct = itemView.findViewById(R.id.btnViewProduct);
        }
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context c) {
            gestureDetector = new GestureDetector(c, new GestureListener());
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            // Determines the fling velocity and then fires the appropriate swipe event accordingly
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeDown();
                            } else {
                                onSwipeUp();
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeUp() {
        }

        public void onSwipeDown() {
        }

    }
}
