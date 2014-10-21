package client.little_practice;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by lou on 2014/10/14.
 * class ParseClient
 */


public class ParseClient {

    public final static int THUMB_WIDTH = 100;
    public final static int THUMB_HEIGHT = 100;
    public final static int QUERY_LIMIT = 100;

    // constructor
    public ParseClient(Activity this_act){

        // initialize Parse
        Parse.initialize( this_act, "msUmCCBdKR3soqTtjXPoMG4xH4LKnwFwvehTjb4r", "x0Weh8Rojf7Xy7kdQlAsnYyxqHigie6vteFKeQID");

    }


    // function to upload an image
    public boolean Upload(Activity myact, String imgname, Bitmap image){

        // user login
        ParseUser currentUser = ParseUser.getCurrentUser();
        if( currentUser != null ){
            // user active

        }else{
            // prompt login
            // TODO: facebook integration
            //Intent intent = new Intent(myact, fb_login.class);
            //myact.startActivity(intent);
        }

        // create thumbnails
        Bitmap thumb = ThumbnailUtils.extractThumbnail(image, THUMB_WIDTH, THUMB_HEIGHT);

        // create byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] origin_byte = stream.toByteArray();
        stream.reset();
        thumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] thumb_byte = stream.toByteArray();

        // create ParseFiles for original image and thumbnail
        ParseFile parseimg_thumb = new ParseFile(imgname, thumb_byte);
        ParseFile parseimg_origin = new ParseFile(imgname, origin_byte);

        // ParseObject for original image
        class myString{
            public String str;

        }
        final myString id = new myString();
        final ParseObject parseobj_origin = new ParseObject("Img");
        parseobj_origin.put("Name", imgname);
        parseobj_origin.put("Img", parseimg_origin);
        parseobj_origin.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if ( e == null ) {
                    // success!
                    Log.d("save!!", "success");
                    id.str = parseobj_origin.getObjectId();
                }else{
                    // failed!!
                    Log.d("save!!", "failed");
                    // TODO : handle exception

                }
            }
        });
        Log.d(imgname, "log!11222122!!");

        // ParseObject for thumbnail, Thunmnails objects only store ObjectID of its original image
        ParseObject parsethumb = new ParseObject("Thumbnails");
        parsethumb.put("Name", imgname);
        parsethumb.put("isThumb", true);
        parsethumb.put("Img", parseimg_thumb);
        if ( id.str.length() == 0 ){
            id.str = "111";
        }
        parsethumb.put("ImgID", id.str);
        parsethumb.saveInBackground();

        return true;
    }




    // fetch all thumbnails
    public List<ParseObject> getImages(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Thumbnails");
        query.setLimit(QUERY_LIMIT);
        query.include("user");
        query.whereEqualTo("isThumb", true);
        List<ParseObject> result = new ArrayList<ParseObject>();
        try {
            result = query.find();

        }catch(ParseException e){
            // TODO: handle query exception
        }
        return result;

    }

}
