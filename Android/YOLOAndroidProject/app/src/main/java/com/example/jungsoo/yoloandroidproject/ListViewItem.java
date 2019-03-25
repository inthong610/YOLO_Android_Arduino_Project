package com.example.jungsoo.yoloandroidproject;

import android.graphics.Bitmap;

public class ListViewItem { // listView는 같은 아이템이 반복될 때 사용.


    private String name;
    private String image_url;
    private String time;
    private String camera;
    private int icon;


    public ListViewItem(String name, String image_url, String time, String camera, int icon) {

        this.name = name;
        this.image_url = image_url;
        this.time = time;
        this.camera = camera;
        this.icon = icon;

    }


    public String getName() {

        return name;

    }

    public String getImage() {

        return image_url;

    }

    public String getTime() {

        return time;

    }

    public String getCamera() {

        return camera;

    }

    public int getIcon() {

        return icon;

    }

    public String getAddress() {

        if (camera.equals("컬러 카메라")) {

            return "충북 보은군 동학로";

        } else if  (camera.equals("적외선 카메라")) {

            return "강원 인제군 북면";

        } else {

            return "위치를 알 수 없습니다.";

        }

    }


}