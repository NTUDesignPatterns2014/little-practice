package client.little_practice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import design.software.little_practice.R;

public class client extends Activity {
    private static final String TAG = "client activity";

    ImageView mImageView;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        mImageView = (ImageView)findViewById(R.id.mImageView);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Parse.initialize(this, "msUmCCBdKR3soqTtjXPoMG4xH4LKnwFwvehTjb4r", "x0Weh8Rojf7Xy7kdQlAsnYyxqHigie6vteFKeQID");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mBitmap);
        }
        else {
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void startCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    public void upload(View MyView){
        if (mBitmap == null) {
            toast("Please take a picture first!");
            return;
        }
        Parse.initialize(this, "msUmCCBdKR3soqTtjXPoMG4xH4LKnwFwvehTjb4r", "x0Weh8Rojf7Xy7kdQlAsnYyxqHigie6vteFKeQID");
        final ParseClient pclient = new ParseClient(this);
        final Activity thisActivity = this;

        int result = pclient.Upload(this, "first image", mBitmap);
        if (result == 0) {
            toast("upload succeeded!");
        } else if (result == -1) {
            //startActivity(new Intent(this, fb_login.class));
            Log.i(TAG, "login facebook");
            toast("loginning facebook");
            ParseFacebookUtils.initialize(getResources().getString(R.string.fb_app_id));
            ParseFacebookUtils.logIn(this, new LogInCallback() {

                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        Log.d("little_practice", "Uh oh. The user cancelled the Facebook login." + err.getLocalizedMessage());
                    } else if (user.isNew()) {
                        Log.d("little_practice", "User signed up and logged in through Facebook!");
                        pclient.Upload(thisActivity, "first image", mBitmap);
                    } else {
                        Log.d("little_practice", "User logged in through Facebook!");
                        pclient.Upload(thisActivity, "first image", mBitmap);
                    }
                }
            });
        }
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
