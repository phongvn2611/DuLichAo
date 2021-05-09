package com.example.virtualtravelapp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.example.virtualtravelapp.R;
import com.squareup.picasso.Picasso;

public class PicassoClient {
    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.get().load(url).placeholder(R.drawable.im_thumbnail).into(img);
        }
        else
        {
            Picasso.get().load(R.drawable.im_thumbnail).into(img);
        }
    }
}
