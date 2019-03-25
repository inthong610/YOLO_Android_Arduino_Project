package com.example.jungsoo.yoloandroidproject;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


// AppCompatActivity는 기본 액티비티. 레이아웃 띄워주는 것.

public class MainActivity extends AppCompatActivity {


    // 액티비티 생명주기. Activity launched -> onCreate() -> onStart -> onResume() -> ::Acticity running:: -> onPause() -> onStop() -> onDestroy() -> ::Activity shut down::
    // onPause()는 화면 터치 안 되게 하는 상태.
    // onStop()는 프로세스에는 살아있는데 백그라운드로 돌아가는 상태. -> onRestart() -> onStart() ex) 다른 앱 실행
    // onStop() 상태일 때 화면 새로고침하고 싶으면 onRestart()이용
    // 앱을 종료하면 onDestroy(). 혹은 화면 넘어갔을 때. 메모리 해제 이런거.


    Handler mHandler; // MainThread에서 실행해야할 것을 MainThread가 아닌 Thread에서 실행 가능하도록 ex) UI 변경은 MainTread에서, 네트워크는 다른 Thread에서 실행해야 함
    ArrayList<ListViewItem> data = new ArrayList<>();
    ListView listview;
    ListviewAdapter adapter;

    String cameraSelected = "all"; // cameraSelected는 camera가 목록 중 선택된 현재 상태. camera=all인 경우
    String animalSelected = "all"; // animalSelected는 animal이 목록 중 선택된 현재 상태.  animal=all인 경우
    String dateSelected = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate는 Activity가 실행될 때 가장 먼저 실행되는 함수.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity main.xml 불러오는 것
        setTitle("활동 알림"); // activity_main layout의 titile bar 이름

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("msg",newToken);

            }
        });


        Spinner camera = (Spinner)findViewById(R.id.camera_selectbox); // Spinner라는 타입의 wildlife 변수에 wildlife_selectbox id를 불러와라
        ArrayAdapter camera_adapter = ArrayAdapter.createFromResource(this,R.array.camera_name,android.R.layout.simple_spinner_item); // String.xml에 저장한 걸 Spinner에 맞도록 변환해주는 역할(아답타!!)
        camera_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        camera.setAdapter(camera_adapter);
        camera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if(selectedItem.equals("모든 카메라")){
                    cameraSelected = "all";
                    new MyTask().execute(); // background에서 실행되게 함.
                }else if(selectedItem.equals("컬러")){
                    cameraSelected = "0";
                    new MyTask().execute();
                }else if(selectedItem.equals("적외선")){
                    cameraSelected = "1";
                    new MyTask().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        // fineViewById는 Object로 불러와서 Spinner로 형변환 해줘야 함


        Spinner wildlife = (Spinner)findViewById(R.id.wildlife_selectbox); // Spinner라는 타입의 wildlife 변수에 wildlife_selectbox id를 불러와라

        // String.xml에 저장한 걸 Spinner에 맞도록 변환해주는 역할(아답타!!)

        // android.R은 안드로이드에서 기본적으로 제공하는 sdk(그 중에 UI)
        //this는 MainActivity를 뜻함. R.array.wildlife_name은 string array.
        // context : 안드로이드에서 기본적으로 제공하는 android.R.layout.simple_spinner_item) UI를 String array list와 합치고 MainActivity와 연결.


        ArrayAdapter wildlife_adapter = ArrayAdapter.createFromResource(this,R.array.wildlife_name,android.R.layout.simple_spinner_item); // String.xml에 저장한 걸 Spinner에 맞도록 변환해주는 역할(아답타!!)

        // 삼각형 눌렀을 때 나오는걸 안드로이드에서 기본적으로 제공하는 레이아웃으로 만들어라.

        wildlife_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // wildlife Spinner에 adapter를 연결해라

        wildlife.setAdapter(wildlife_adapter);


        // https://stackoverflow.com/questions/12108893/set-onclicklistener-for-spinner-item
        // 에서 spinner.setOnItemSelectedListener(new OnItemSelectedListener() 부분


        wildlife.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();

                if(selectedItem.equals("모든 동물")){
                    animalSelected = "all";
                    new MyTask().execute(); // background에서 실행되게 함.
                }else if(selectedItem.equals("멧돼지")){
                    animalSelected = "0";
                    new MyTask().execute();
                }else if(selectedItem.equals("고라니")){
                    animalSelected = "1";
                    new MyTask().execute();
                }else if(selectedItem.equals("곰")){
                    animalSelected = "2";
                    new MyTask().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //

        Spinner date = (Spinner)findViewById(R.id.date_selectbox); // Spinner라는 타입의 wildlife 변수에 wildlife_selectbox id를 불러와라

        ArrayAdapter date_adapter = ArrayAdapter.createFromResource(this,R.array.date_name,android.R.layout.simple_spinner_item); // String.xml에 저장한 걸 Spinner에 맞도록 변환해주는 역할(아답타!!)

        // 삼각형 눌렀을 때 나오는걸 안드로이드에서 기본적으로 제공하는 레이아웃으로 만들어라.

        date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // wildlife Spinner에 adapter를 연결해라

        date.setAdapter(date_adapter);


        // https://stackoverflow.com/questions/12108893/set-onclicklistener-for-spinner-item
        // 에서 spinner.setOnItemSelectedListener(new OnItemSelectedListener() 부분


        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();

                if(selectedItem.equals("모든 날짜")){
                    dateSelected = "all";
                    new MyTask().execute(); // background에서 실행되게 함.
                }else
                if(selectedItem.equals("2018-09-19")){
                    dateSelected = "2018-09-19 | 17:36";
                    new MyTask().execute(); // background에서 실행되게 함.
                }else if(selectedItem.equals("2018-09-22")){
                    dateSelected = "2018-09-22 | 14:30";
                    new MyTask().execute();
                }else if(selectedItem.equals("2018-09-24")){
                    dateSelected = "2018-09-24 | 20:39";
                    new MyTask().execute();
                }else if(selectedItem.equals("2018-09-27")){
                    dateSelected = "2018-09-27 | 11:50";
                    new MyTask().execute();
                }else if(selectedItem.equals("2018-09-30")){
                    dateSelected = "2018-09-30 | 01:15";
                    new MyTask().execute();
                }else if(selectedItem.equals("2018-10-08")){
                    dateSelected = "2018-10-08 | 17:25";
                    new MyTask().execute();
                }else if(selectedItem.equals("2018-10-09")){
                    dateSelected = "2018-10-09 | 16:57";
                    new MyTask().execute();
                }

                else{
                    new MyTask().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        //



        // adapter 빈거 연결 하는 부분. 데이터 변경되면 여기 adapter 변경 됨.

        listview = (ListView)findViewById(R.id.mainactivity_listview);
       adapter = new ListviewAdapter(this, R.layout.list_view_item, data);
        listview.setAdapter(adapter);


      //  Spinner date = (Spinner)findViewById(R.id.date_selectbox); // Spinner라는 타입의 wildlife 변수에 wildlife_selectbox id를 불러와라
       // ArrayAdapter date_adapter = ArrayAdapter.createFromResource(this,R.array.date_name,android.R.layout.simple_spinner_item); // String.xml에 저장한 걸 Spinner에 맞도록 변환해주는 역할(아답타!!)
       // date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //date.setAdapter(date_adapter);

  //      new MyTask().execute();


    }

   // 이 주소에서 MyTask부분 참고. https://androidcource.blogspot.com/2017/09/okhttp-with-listview-json-example-in.html

    public class MyTask extends AsyncTask<Void,Void,String>{ // AsyncTask는 안드로이드에서 기본 제공.
                                                             // thread 따로 생성 않고 background에서 실행할 수 있도록 해주는 클래스

        @Override
        protected String doInBackground(Void... voids){ // background에서 실행하기

            try{
                OkHttpClient client = new OkHttpClient(); // Open Library

                // string으로 준 url은 getString으로 가져온다.
                // 밑의 코드는 메인화면에 처음 모든 정보 보여주는 쿼리.
                Request request = new Request.Builder().url((getString(R.string.url)+
                        "/yolo.php?camera="+cameraSelected+"&animal="+animalSelected+"&date="+dateSelected)).get().build(); // Open Library

                Response response = client.newCall(request).execute(); // Open Library
           //     Log.d("msg", response.body().string());

                JSONArray result = new JSONArray(response.body().string()); // reponse.body().string()만 Open Library
                data.clear(); // 이거 안 하면 밑에 계속 생김

                for(int i=0; i<result.length(); i++) {


                    String name;
                    String imageUrl = result.getJSONObject(i).getString("picture"); // picture, date, animal, camera는 php와 통일한 것
                    String camera;
                    String date = result.getJSONObject(i).getString("date");
                    int iconAddress;

                    if (result.getJSONObject(i).getInt("animal") == 0) {
                        name = "멧돼지";
                        iconAddress = R.drawable.wildboar_icon;
                    } else if (result.getJSONObject(i).getInt("animal") == 1) {
                        name = "고라니";
                        iconAddress = R.drawable.inermis_icon;

                    } else if (result.getJSONObject(i).getInt("animal") == 2) {
                        name = "곰";
                        iconAddress = R.drawable.bear_icon;

                    } else {
                        Log.d("msg", result.getJSONObject(i).getInt("animal")+"");
                        name = "오류";
                        iconAddress = R.drawable.wildboar_icon;
                    }


                    if (result.getJSONObject(i).getInt("camera") == 0) {
                        camera = "컬러 카메라";
                    } else if (result.getJSONObject(i).getInt("camera") == 1) {
                        camera = "적외선 카메라";
                    } else {
                        camera = "오류";
                    }


                    // 서버에서 받은거 넣을 것. activity가 켜지고 onCreate()가 실행되면서 서버에 로그 기록 요청. 서버에서 json으로 응답해줌.
                    ListViewItem temp = new ListViewItem(name, imageUrl, date, camera, iconAddress);

                    data.add(temp);

                }


            }catch (IOException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }


            return null;

        }



        @Override
        protected void onPostExecute(String aVoid){ // doInBackground함수 실행 후 실행되는 함수

            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged(); // Adapter에 data가 바뀌었다고 알려줌. (원래는 onCreate실행때문에 Adapter 빈채로 listview에 이용됐다가 변경되면 변경됐다고 알려줌.)

        }
    }
}
