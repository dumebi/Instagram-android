package com.example.kornet_imac_1.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeed extends AppCompatActivity {
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        Intent i = getIntent();
        String activeUsername = i.getStringExtra("username");

        Log.i("AppInfo", activeUsername);
        setTitle(activeUsername+"'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        //query.whereEqualTo("username", activeUsername);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.i("AppInfo", String.valueOf(objects));
                    if(objects.size() > 0){
                        for(ParseObject object : objects){
                            ParseFile file = (ParseFile)object.get("images");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setImageBitmap(image);
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));
                                        linearLayout.addView(imageView);
                                    }
                                }
                            });
                        }
                    }else{
                        Log.i("AppInfo - object", "zero");
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_feed, menu);
        return true;
    }
}
