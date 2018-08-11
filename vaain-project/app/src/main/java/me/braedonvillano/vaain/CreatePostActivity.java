package me.braedonvillano.vaain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Post;
import me.braedonvillano.vaain.models.Product;

public class CreatePostActivity extends AppCompatActivity {

    private ImageView image;
    private Button button;
    private TextView description;
    private RecyclerView rvProducts;

    private ParseUser user;
    private Post newPost;
    private File photoFile;
    private ParseFile parseImage;
    private Product relatedProduct;
    private ArrayList<Product> products;
    private AddProductAdapter productAdapter;

    public final String APP_TAG = "Vaain";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 1046;
    public String photoFileName = "vaain-photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openMediaSelectionDialogue();
        setContentView(R.layout.activity_create_post);

        image = findViewById(R.id.ivPostImage);
        button = findViewById(R.id.btnCreatePost);
        rvProducts = findViewById(R.id.rvAddProduct);
        description = findViewById(R.id.mtPostDescription);

        user = ParseUser.getCurrentUser();
        products = new ArrayList<>();
        relatedProduct = new Product();
        productAdapter = new AddProductAdapter(products);
        rvProducts.setAdapter(productAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });

        fetchProducts();
    }

    public void fetchProducts() {
        final Product.Query productQ = new Product.Query();
        productQ.whereEqualTo("beaut", user);

        productQ.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                if (e == null) {
                    products.addAll(objects);
                    productAdapter.addAll(products);
                } else {
                    e.printStackTrace();
                }

            }
        });
    }

    private void openMediaSelectionDialogue() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Add Picture");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        onLaunchCamera();
                    }
                });

        myAlertDialog.setNegativeButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        openGallery();
                    }
                });
        myAlertDialog.show();
    }

    public void createPost() {
        Post post = new Post();

        // TODO: check for empty fields before submission
        if (productAdapter.selectedProduct == null) {
            Toast.makeText(this, "No Product Selected", Toast.LENGTH_LONG).show();
            return;
        }

        post.setDescription(description.getText().toString());
        post.setProduct(productAdapter.selectedProduct);
        post.setImage(parseImage);
        newPost = post;

        savePost();
    }

    public void savePost() {
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(CreatePostActivity.this, "Post Added", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /* below is the intent logic for the camera selection */
    public void onLaunchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(this, "me.vaain.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    /* below is the intent logic for gallery selection */
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    /* onActivityResult below handles the flow logic for the create process */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Intent intent = new Intent(CreatePostActivity.this, BeautMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(this, "No Picture Added!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                image.setImageBitmap(bitmap);
                parseImage = new ParseFile(photoFile);
                break;
            }
            case SELECT_IMAGE_ACTIVITY_REQUEST_CODE: {
                if (data == null) break;
                Bitmap bitmap = null;
                Uri photoUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    image.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                    byte[] imageByte = byteArrayOutputStream.toByteArray();
                    parseImage = new ParseFile("product_image.png", imageByte);
                }
                break;
            }
            default:
                Log.e("OnActivityResult", "The requestCode did not match any case!");
                break;
        }
    }
}
