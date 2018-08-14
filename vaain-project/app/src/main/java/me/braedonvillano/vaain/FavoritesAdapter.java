package me.braedonvillano.vaain;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{
    private List<Product> products;
    private Context context;

    public FavoritesAdapter(List<Product> mProducts, ClientFavoritesFragment clientFavoritesFragment) {
        products = new ArrayList<>();
        products.addAll(mProducts);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View favoriteView = layoutInflater.inflate(R.layout.item_beaut_product, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(favoriteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        if(product.getImage() != null) Glide.with(context).load(product.getImage().getUrl()).into(viewHolder.ivProductImage);

    }

    public void clearAll() { products.clear(); }

    public void addAll(List<Product> newProducts) {
        products.addAll(newProducts);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            ivProductImage.setMaxWidth(width/3);
            ivProductImage.setMaxHeight(width/3);
            ivProductImage.setMinimumHeight(width/3);
            ivProductImage.setMinimumWidth(width/3);


        }
    }
}
