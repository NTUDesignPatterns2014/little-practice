package client.little_practice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import design.software.little_practice.R;

public class fb_login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_login);
        Log.d("act/fb_login", "hihihi");
        ParseFacebookUtils.initialize("818196911534202");
        ParseFacebookUtils.logIn(this, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("little_practice", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("little_practice", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("little_practice", "User logged in through Facebook!");
                }

            }
        });
        //startActivity(new Intent(this, client.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fb_login, menu);
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
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
