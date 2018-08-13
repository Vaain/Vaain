package me.braedonvillano.vaain;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import me.braedonvillano.vaain.models.Product;




public class BeautsProductsAdapter extends RecyclerView.Adapter<BeautsProductsAdapter.ViewHolder>  {

    List<Product> products;
    Context context;

    static Callback callback;

    public BeautsProductsAdapter(List<Product> newProducts, Callback callback1){
        products = newProducts;
        callback = callback1;
    }


    public interface Callback {
        void onRequestProduct(Product product, int code);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_beaut_product, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = products.get(i);

        if(product.getImage() != null)Glide.with(context).load(product.getImage().getUrl()).into(viewHolder.ivProductImage);

        viewHolder.tvProName.setText(product.getName());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProductImage;
        Dialog myDialog;
        TextView tvProName;

        public ViewHolder(View productView) {
            super(productView);
            ivProductImage = productView.findViewById(R.id.ivProductImage);
            tvProName = productView.findViewById(R.id.tvProName);
            // Dialog ini
            myDialog = new Dialog(context);
            myDialog.setContentView(R.layout.item_home);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            ivProductImage.setMaxWidth(width/3);
            ivProductImage.setMaxHeight(width/3);
            ivProductImage.setMinimumHeight(width/3);
            ivProductImage.setMinimumWidth(width/3);

            productView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Product curProd = products.get(getAdapterPosition());

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
                    Product curProd = products.get(getAdapterPosition());
                    callback.onRequestProduct(curProd,SearchProductsAdapter.REQUEST_CODE);
                    myDialog.dismiss();
                }

            });
            myDialog.show();
            dlgProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product curProd = products.get(getAdapterPosition());
                    callback.onRequestProduct(curProd,SearchProductsAdapter.PROFILE_CODE);
                    myDialog.dismiss();
                    Log.d("request","beaut clicked");
                }
            });
        }
    }
}

