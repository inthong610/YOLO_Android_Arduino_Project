package com.example.jungsoo.yoloandroidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;


//[안드로이드 ImageView에 url 이미지로 보여주기]
// http://istoryful.tistory.com/72
// 이건 이미지 요청 끝날 때까지 bitmap 받아오는거 백그라운드로 이미지 처리 되는거
// AsyncTask는 안드로이드에서 기본으로 제공.

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;


    public DownloadImageTask(ImageView bmImage){
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls){

        String urldisplay = urls[0];
        Bitmap mlcon11 = null;

        try{

            InputStream in = new java.net.URL(urldisplay).openStream();
            mlcon11 = BitmapFactory.decodeStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mlcon11;
    }

    // result 화면에 뿌려주기

    protected void onPostExecute(Bitmap result){
        bmImage.setImageBitmap(result);
    }

}
