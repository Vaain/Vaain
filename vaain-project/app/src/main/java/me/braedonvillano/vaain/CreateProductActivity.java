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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import me.braedonvillano.vaain.models.Product;

import static me.braedonvillano.vaain.WorkSchedules.getAllTags;

public class CreateProductActivity extends AppCompatActivity {

    private ImageView image;
    private Button button;
    private TextView name;
    private TextView price;
    private TextView description;
    private ListView lvTags;
    private ListView lvSetTags;

    private ParseUser user;
    private Product newProduct;
    private File photoFile;
    private ParseFile parseImage;
    private ArrayList<String> allTags;
    private ArrayList<String> setTags;
    ArrayAdapter<String> tagsAdapter;
    ArrayAdapter<String> setTagsAdapter;

    public final String APP_TAG = "Vaain";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 1046;
    public String photoFileName = "vaain-photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openMediaSelectionDialogue();
        setContentView(R.layout.activity_create_product);

        user = ParseUser.getCurrentUser();

        name = findViewById(R.id.tvProductName);
        price = findViewById(R.id.tvProductPrice);
        image = findViewById(R.id.ivProductImage);
        button = findViewById(R.id.btnCreateProduct);
        lvTags = findViewById(R.id.lvTags);
        lvSetTags = findViewById(R.id.lvSetTags);
        description = findViewById(R.id.mtProductDescription);

        setTags = new ArrayList<>();
        allTags = new ArrayList<>();
        allTags.addAll(Arrays.asList(getAllTags()));

        tagsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allTags);
        setTagsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, setTags);
        lvTags.setAdapter(tagsAdapter);
        lvSetTags.setAdapter(setTagsAdapter);
        setupAllTagsListener();
        setupSetTagsListener();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProduct();
            }
        });
    }

    private void setupAllTagsListener() {
        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                // should be adding the tags to the product
                String tag = String.valueOf(parent.getItemAtPosition(i));
                setTags.add(tag);
                setTagsAdapter.notifyDataSetChanged();

                allTags.remove(i);
                tagsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupSetTagsListener() {
        lvSetTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                // should be adding the tags to the product
                String tag = String.valueOf(parent.getItemAtPosition(i));
                allTags.add(tag);
                tagsAdapter.notifyDataSetChanged();

                setTags.remove(i);
                setTagsAdapter.notifyDataSetChanged();
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

    public void createProduct() {
        Product prod = new Product();

        if (price.getText().toString() == "") {
            Toast.makeText(this, "Add Price!", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: check for empty fields before submission

        prod.setDescription(description.getText().toString());
        prod.setName(name.getText().toString());
        prod.setPrice(Float.parseFloat(price.getText().toString()));
        prod.setImage(parseImage);
        prod.setBeaut(user);
        prod.addAll("tagList", setTags);
        newProduct = prod;

        saveProduct();
    }

    public void saveProduct() {
        newProduct.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("BusinessFragment", "Created Product");
                    addProductToUser();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addProductToUser() {
        user.add("products", newProduct);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(CreateProductActivity.this, "Product Added", Toast.LENGTH_LONG).show();
                // TODO: need to make sure that the product is added to the list at end -- will come back
                // Intent data = new Intent();
                // setResult(RESULT_OK, data);
                finish();
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
            Intent intent = new Intent(CreateProductActivity.this, BeautMainActivity.class);
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
