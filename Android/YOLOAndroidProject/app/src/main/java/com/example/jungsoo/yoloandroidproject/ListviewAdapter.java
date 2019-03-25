package com.example.jungsoo.yoloandroidproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class ListviewAdapter extends BaseAdapter{ // Adapter는 listview와 itemview에서 item을 변환해서 서로 연결해주는거.


    private LayoutInflater inflater;
    private ArrayList<ListViewItem> data;
    private int layout;
    private Context context;

    // Context는 레이아웃만 관여하는 것. 화면이 띄워져 있는 상태에서 돌아가고 있는 것.


    public ListviewAdapter(Context context, int layout, ArrayList<ListViewItem> data){

        this.context = context; // Adapter에는 context없으니(context는 activity에 있음) 끌어오기 위함.
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;

    }

    @Override
    public int getCount(){

        return data.size();

    }

    @Override
    public String getItem(int position){

        return data.get(position).getName();

    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    // listview안에 들어가는 item 하나의 레이아웃을 불러와서 커스터마이징 하는 부분.

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView==null){ // convertView는 리스트뷰 하나(한줄)

            convertView = inflater.inflate(layout, parent, false);

        }

        final ListViewItem listViewItem = data.get(position);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // click 됐을 때 실행

                // 안드로이드에서 화면 전환할 때는 Intent 사용. context에서 DetailActivity 화면으로 전환 됨.
                Intent intent = new Intent(context, DetailActivity.class );
                // time 이라는 변수에 데이터 넣음. activity에서 이걸로 불러오며 됨.
                intent.putExtra("time", listViewItem.getTime()); // 화면 전환 될 때 intent에 값 넣어서 전환
                intent.putExtra("camera", listViewItem.getCamera());
                intent.putExtra("kind", listViewItem.getName());
                intent.putExtra("icon", listViewItem.getIcon());
                intent.putExtra("bitmap_url", listViewItem.getImage());
                intent.putExtra("address", listViewItem.getAddress() );


                context.startActivity(intent);

            }
        });



        TextView name = (TextView)convertView.findViewById(R.id.notify_item_text);
        name.setText(listViewItem.getName());

        TextView camera = (TextView)convertView.findViewById(R.id.camera_item_text);
        camera.setText(listViewItem.getCamera());

        TextView address = (TextView)convertView.findViewById(R.id.address_item_text);
        address.setText(listViewItem.getAddress());

        TextView date = (TextView)convertView.findViewById(R.id.time_item_text);
        date.setText(listViewItem.getTime());


        ImageView image = (ImageView)convertView.findViewById(R.id.listview_item_image);
       // image.setImageResource(listViewItem.getImage()); 이건 소스로 이미지 저장해서 테스트했던거


        new DownloadImageTask(image).execute(listViewItem.getImage()); // url 받아온거 백그라운드에서 실행하기. 클래스 돌아가서 실행 됨.



        ImageView icon_image = (ImageView)convertView.findViewById(R.id.icon_image);
        icon_image.setImageResource(listViewItem.getIcon());


        return convertView;




    }


}
