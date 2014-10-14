package client.little_practice;


import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

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
    public boolean Upload(String imgname, Bitmap image){

        // user login
        ParseUser currentUser = ParseUser.getCurrentUser();
        if( currentUser != null ){
            // user active

        }else{
            // prompt login
            // TODO: facebook integration
        }

        // create thumbnails
        Bitmap thumb = ThumbnailUtils.extractThumbnail(image, THUMB_WIDTH, THUMB_HEIGHT);

        // create ParseFiles for original image and thumbnail
        byte[] thumb_byte = new byte[100];// TODO : convert Bitmap to byte[]
        byte[] origin_byte = new byte[100];// TODO : convert Bitmap to byte[]
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
                    id.str = parseobj_origin.getObjectId();
                }else{
                    // failed!!
                    // TODO : handle exception

                }
            }
        });

        // ParseObject for thumbnail, Thunmnails objects only store ObjectID of its original image
        ParseObject parsethumb = new ParseObject("Thumbnails");
        parsethumb.put("Name", imgname);
        parsethumb.put("isThumb", true);
        parsethumb.put("Img", parseimg_thumb);
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
