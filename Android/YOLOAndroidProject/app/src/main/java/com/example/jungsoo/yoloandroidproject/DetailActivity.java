package com.example.jungsoo.yoloandroidproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


// activity 새로 만들면 Manifest에 넣어줘야 함

public class DetailActivity extends AppCompatActivity {


    Handler mHandler; // MainThread에서 실행해야할 것을 MainThread가 아닌 Thread에서 실행 가능하도록 ex) UI 변경은 MainTread에서, 네트워크는 다른 Threadㅇ에서 실행해야 함

    @Override

    protected void onCreate(Bundle savedInstanceState) { // onCreate는 Activity가 실행될 때 가장 먼저 실행되는 함수.

        super.onCreate(savedInstanceState);
        mHandler = new Handler();

        setContentView(R.layout.detail_image_view); // activity main.xml 불러오는 것
        setTitle("상세 보기"); // activity_main layout의 titile bar 이름

        Intent intent = getIntent(); // intent로 받아온 값 보여주기
        String time = intent.getStringExtra("time");
        TextView date_textview = findViewById(R.id.detail_time);
        date_textview.setText(time);

        String camera = intent.getStringExtra("camera");
        TextView camera_textview = findViewById(R.id.detail_camera);
        camera_textview.setText(camera);

        String address = intent.getStringExtra("address");
        TextView address_textview = findViewById(R.id.detail_address);
        address_textview.setText(address);

        String kind = intent.getStringExtra("kind");
        TextView kind_textview = findViewById(R.id.detail_kind);
        kind_textview.setText(kind+" 출현 감지");

        int icon = intent.getIntExtra("icon", 5);
        ImageView icon_imageview = findViewById(R.id.detail_icon);
        icon_imageview.setImageResource(icon);

        ImageView detail_image = findViewById(R.id.detail_image); // resource 아이디로 url 이미지 불러오기
        new DownloadImageTask(detail_image).execute(intent.getStringExtra("bitmap_url")); // url 받아온거 백그라운드에서 실행하기. 클래스 돌아가서 실행 됨.



        // 버튼 이벤트 구현 (cancel, ok)


        Button cancel_button; // cancel 버튼 누르면 activity_main 나오게

            cancel_button = (Button)findViewById(R.id.cancel_button);
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();

                }
            });

        // okhttp 참고 : http://square.github.io/okhttp/

        Button ok_button; // ok 버튼 눌렀을 때.

            ok_button = (Button)findViewById(R.id.ok_button);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject obj;

                    new Thread(){ // 이건 네트워크 요청. ui빼고는 MainThread에서 하면 안 되어서 Thread 새로 만듬
                      @Override
                      public void run(){  // Open Library OkHttp 사용해서 http 요청을 서버에 한 것. (OkHttp 안 쓰면 url connection 복잡해짐)
                          try{

                              OkHttpClient client = new OkHttpClient(); // Open Library
                              // alert 변수에 temp string 값 담아져서 post로 서버에 보내짐. {alert:"temp"}
                              RequestBody body = new FormBody.Builder().add("alert","temp").build();


                              Request request = new Request.Builder().url(getString(R.string.url)+"/yolo2.php").post(body).build();
                              Response response = client.newCall(request).execute();

                   //           final JSONObject obj = new JSONObject(response.body().string()); // string으로 넘어온걸 json으로 바꿔줌

                              mHandler.post(new Runnable() {
                                  @Override
                                  public void run() {

                                      AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
                                      alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialogInterface, int i) {
                                              dialogInterface.dismiss(); // 창을 닫기

                                          }
                                      });

                          /*            try{ // obj는 json이므로

                                        Log.d("msg", obj.getString("result"));

                                          if(obj.getString("result")=="success"){


                                          }else{

                                              alert.setMessage("요청이 실패하였습니다.");

                                          }
                                      }catch (JSONException e){
                                          e.printStackTrace();
                                      }

*/
                                        alert.setMessage("요청이 완료되었습니다.");
                                        alert.show();


                                  }
                              });


                          }catch (IOException e){
                              e.printStackTrace();
                          } catch (Exception e){
                              e.printStackTrace();
                          }

                      }
                    }.start(); // .start() 잊지 말 것!!!

                }
            });

        }


}
