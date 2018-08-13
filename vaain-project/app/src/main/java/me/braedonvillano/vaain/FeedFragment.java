package me.braedonvillano.vaain;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.braedonvillano.vaain.models.Post;


public class FeedFragment extends Fragment {
    private ParseUser user;
    private ArrayList<Post> posts;
    private FeedAdapter feedAdapter;

    private RecyclerView rvFeed;

    public FeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        posts = new ArrayList<>();
        user = ParseUser.getCurrentUser();
        loadPosts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        rvFeed = view.findViewById(R.id.rvFeed);
        feedAdapter = new FeedAdapter(posts);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeed.setAdapter(feedAdapter);

        return view;
    }

    public void loadPosts() {
        final Post.Query postQ = new Post.Query().withBeaut();
        postQ.whereContainedIn("beaut", (Collection<ParseUser>) user.get("follow"));

        postQ.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
//                        Log.d("*******", objects.get(i).toString());
                    }
                    posts.addAll(objects);
                    feedAdapter.addAll(posts);
                    feedAdapter.notifyItemRangeInserted(0,posts.size());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}