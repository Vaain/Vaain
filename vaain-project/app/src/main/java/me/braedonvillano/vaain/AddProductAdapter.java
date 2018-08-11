package me.braedonvillano.vaain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.ViewHolder> {
    static List<Product> mProducts;
    static Context context;
    public static Product selectedProduct = null;

    public interface Callback {
        void onProductClicked(Product post);
    }

    public AddProductAdapter(List<Product> products) {
        mProducts = new ArrayList<>();
        mProducts.addAll(products);
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

        // TODO: figure out how to handle recycled views without this -- still not working
        if (selectedProduct == null) {
            holder.tvOverlay.setVisibility(View.INVISIBLE);
        } else if (selectedProduct == product) {
            holder.tvOverlay.setVisibility(View.VISIBLE);
        }

        holder.tvProductName.setText(product.getName());

        if (product.getImage() != null) {
            Glide.with(context)
                    .load(product.getImage().getUrl())
                    .into(holder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProductImage;
        public TextView tvProductName;
        public TextView tvOverlay;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvOverlay = itemView.findViewById(R.id.tvOverlay);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // set selected product and remove the rest
                    Product curProduct = mProducts.get(getAdapterPosition());
                    if (selectedProduct == null) {
                        selectedProduct = curProduct;
                        tvOverlay.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Selected!", Toast.LENGTH_LONG).show();
                    } else if (selectedProduct == curProduct) {
                        selectedProduct = null;
                        tvOverlay.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Deselected!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Must Deselect!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}