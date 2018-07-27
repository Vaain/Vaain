package me.braedonvillano.vaain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import me.braedonvillano.vaain.models.Product;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.ViewHolder>  {

    static List<Product> mProducts;
    public Context context;
    private Dialog myDialog;
    static Callback callback;

    public interface Callback {
        void onRequestProduct(Product product);
    }


    public SearchProductsAdapter(List<Product> products, @NonNull final Callback callback) {
        mProducts = products;
        this.callback = callback;
    }

    public void clear() {
        mProducts.clear();
        notifyDataSetChanged();
    }

    // add a list of items -- change to type used
    public void addAll(List<Product> list) {
        mProducts.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Product product) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int b) {
        context = viewGroup.getContext();
        LayoutInflater i = LayoutInflater.from(context);
        View view = i.inflate(R.layout.item_home_grid, viewGroup, false);
        //view.setOnClickListener(mClickListener);

        final ViewHolder vHolder = new ViewHolder(view);

        // Dialog ini
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.item_home);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        vHolder.rlHomeGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product curProd = mProducts.get(vHolder.getAdapterPosition());

                ImageView dlgProfilePic = myDialog.findViewById(R.id.ivDProfilePic);
                TextView dlgBeautName = myDialog.findViewById(R.id.tvDiaBeautName);
                TextView dlgPrice = myDialog.findViewById(R.id.tvDPrice);
                TextView dlgDescription = myDialog.findViewById(R.id.tvDDescript);
                ImageView dlgProductPic = myDialog.findViewById(R.id.ivDProduct);
                Button dlgRequest = myDialog.findViewById(R.id.btnDRequest);
                final TextView dlgProductName = myDialog.findViewById(R.id.tvDProName);


                dlgBeautName.setText(curProd.getBeaut().getUsername());
                dlgPrice.setText(curProd.getPrice().toString());
                dlgDescription.setText(curProd.getDescription());
                dlgProductName.setText(curProd.getName());

                // add profile and product pic to modal
                Glide.with(context)
                        .load(curProd.getImage()
                        .getUrl())
                        .into(dlgProductPic);
                Glide.with(context)
                        .load(curProd.getBeaut().getParseFile("profileImage")
                        .getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(dlgProfilePic);

                dlgRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Product curProd = mProducts.get(vHolder.getAdapterPosition());
                            callback.onRequestProduct(curProd);
                            myDialog.dismiss();
                        }

                    });
                    myDialog.show();
                }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Product product = mProducts.get(position);

        viewHolder.tvBeautName.setText(product.getBeaut().getUsername());
        viewHolder.tvProductName.setText(product.getName());
        if (product.getImage() != null) {
            Glide.with(context)
                    .load(product.getImage().getUrl())
                    .into(viewHolder.ivProductImage);
        }
    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlHomeGrid;
        public TextView tvProductName;
        public TextView tvBeautName;
        public ImageView ivProductImage;


        public ViewHolder(View itemView) {
            super(itemView);

            rlHomeGrid = itemView.findViewById(R.id.item_home_grid);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBeautName = itemView.findViewById(R.id.tvBeautName);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);


        }
    }


}
