package me.braedonvillano.vaain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    static List<Product> mProducts;
    static Context context;
    static Callback callback;

    public interface Callback {
        void onProductClicked(Product post);
    }

    public ProductAdapter(ArrayList<Product> posts, @NonNull final Callback callback) {
        mProducts = posts;
        this.callback = callback;
    }

    public ProductAdapter(List<Product> products) {
        mProducts = products;
    }

    // Clean all elements of the recycler
    public void clear() {
        mProducts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Product> list) {
        mProducts.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Product product) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_product, parent, false);

        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Product product = mProducts.get(position);

        product.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null) {
                    holder.tvProductName.setText(product.getName());

                    if (product.getImage() != null) {
                        Glide.with(context)
                                .load(product.getImage().getUrl())
                                .into(holder.ivProductImage);
                    }
                }else {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProductImage;
        public TextView tvProductDescription;
        public TextView tvProductPrice;
        public TextView tvProductName;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callback.onProductClicked(mProducts.get(getAdapterPosition()));
//                }
//            });
        }
    }
}