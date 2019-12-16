package au.edu.staylor.citybuilder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity {
    protected static final int REQUEST_THUMBNAIL = 3;
    protected static final String EXTRA_ROW = "au.edu.staylor.row";
    protected static final String EXTRA_COL = "au.edu.staylor.col";
    protected static final String EXTRA_TYPE = "au.edu.staylor.type";
    protected static final String EXTRA_NAME = "au.edu.staylor.name";
    protected static final String EXTRA_IMG_BYTE_ARRAY = "au.edu.staylor.img";

    private int row;
    private int col;
    private TextView rowCol;
    private TextView elementType;
    private EditText elementName;
    private ImageView imgView;
    private Button photoBtn;
    private Button saveBtn;
    private Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        thumbnail = null;

        Intent intent = getIntent();
        setReferences();
        setOnClicks();

        unpackIntent(intent);

        setUpPicBtn();
    }

    protected void setReferences() {
        rowCol = findViewById(R.id.rowCol);
        elementType = findViewById(R.id.elementType);
        elementName = findViewById(R.id.elementName);
        imgView = findViewById(R.id.detailsImgView);
        photoBtn = findViewById(R.id.photoBtn);
        saveBtn = findViewById(R.id.saveDetails);
    }

    protected void setOnClicks() {
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_THUMBNAIL);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnData = new Intent();
                DetailsActivity.putExtras(returnData, row, col, elementType.getText().toString(), elementName.getText().toString(), thumbnail);

                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    protected void setUpPicBtn() {
        Intent thumbnailIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Disables button if there is no camera option
        PackageManager pm = getPackageManager();
        if (pm.resolveActivity(thumbnailIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            photoBtn.setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_THUMBNAIL) {
            thumbnail = (Bitmap) returnData.getExtras().get("data");
            imgView.setImageBitmap(thumbnail);
        }
    }

    protected void unpackIntent(Intent intent) {
        row = intent.getIntExtra(EXTRA_ROW, -1);
        col = intent.getIntExtra(EXTRA_COL, -1);
        rowCol.setText("Row: " + row + " Col: " + col);

        elementType.setText(intent.getStringExtra(EXTRA_TYPE));
        elementName.setText(intent.getStringExtra(EXTRA_NAME));

        thumbnail = DetailsActivity.getBitMap(intent);
        // thumbnail will be null if unsuccessful
        if (thumbnail != null) imgView.setImageBitmap(thumbnail);
    }

    public static Intent getIntent(Context context, int row, int col, String type, String name, Bitmap thumbnail) {
        Intent intent = new Intent(context, DetailsActivity.class);

        putExtras(intent, row, col, type, name, thumbnail);
        return intent;
    }

    public static void putExtras(Intent intent, int row, int col, String type, String name, Bitmap thumbnail) {
        intent.putExtra(EXTRA_ROW, row);
        intent.putExtra(EXTRA_COL, col);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_NAME, name);

        byte[] byteArray = bitmapToStream(thumbnail);
        intent.putExtra(EXTRA_IMG_BYTE_ARRAY, byteArray);
    }

    public static byte[] bitmapToStream(Bitmap thumbnail) {
        byte[] byteArray = null;

        try {
            // Convert Bitmap into an object we can transfer between activities
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            // Clean up
            if (thumbnail != null && !thumbnail.isRecycled()) {
                thumbnail.recycle();
                thumbnail = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public static int getRow(Intent intent) {
        return intent.getIntExtra(EXTRA_ROW, -1);
    }

    public static int getCol(Intent intent) {
        return intent.getIntExtra(EXTRA_COL, -1);
    }

    public static String getType(Intent intent) {
        return intent.getStringExtra(EXTRA_TYPE);
    }

    public static String getElementName(Intent intent) {
        return intent.getStringExtra(EXTRA_NAME);
    }

    public static Bitmap getBitMap(Intent intent) {
        Bitmap thumbnail = null;
        try {
            byte[] byteArray = intent.getByteArrayExtra(EXTRA_IMG_BYTE_ARRAY);
            thumbnail = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbnail;
    }

}
