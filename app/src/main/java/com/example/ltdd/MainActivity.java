package com.example.ltdd;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String API_KEY="AIzaSyAH1K1jIDerK_zta3-6pinbQcXadb2Ukow";
    public static String ID_PLAYLIST="PLTjpHrsOi7odt8C75nVGHnGpQ9ZESa53z";
    public static String urlGetJson="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + ID_PLAYLIST + "&key=" + API_KEY + "&maxResults=50";

    EditText editText;
    ImageButton imageButton2;
    String key;

    TabHost tabHost;
    ListView lvVideo, listSearchVideo;
    ArrayList<VideoYoutube> arrayVideo, arraySearchList;
    VideoYouTubeAdapter adapter, searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
        String duoi="&type=video&key="+getString(R.string.API_key);
        addControll();
        lvVideo= findViewById(R.id.listviewVideo);
        listSearchVideo= findViewById(R.id.listSearchVideo);
        arrayVideo = new ArrayList<>();
        arraySearchList = new ArrayList<>();
        adapter = new VideoYouTubeAdapter(this,R.layout.row_video_youtube,arrayVideo );
        searchAdapter = new VideoYouTubeAdapter(this,R.layout.row_video_youtube,arraySearchList );
        lvVideo.setAdapter(adapter);
        listSearchVideo.setAdapter(searchAdapter);

        editText = findViewById(R.id.editText);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraySearchList.clear();
                key = editText.getText().toString();
                String link = url + key +duoi;
                searchKey(link);
            }
        });

        listSearchVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(MainActivity.this, PlayVideoActivity.class);
                intent.putExtra("idVideoYoutube",arraySearchList.get(position).getIdVideo());
                intent.putExtra("item", arraySearchList.get(position));
                startActivity(intent);
            }
        });

        GetJsonYoutube(urlGetJson);
        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(MainActivity.this, PlayVideoActivity.class);
                intent.putExtra("idVideoYoutube",arrayVideo.get(position).getIdVideo());
                intent.putExtra("item", arrayVideo.get(position));
                startActivity(intent);
            }
        });
    }

    private void addControll() {
        tabHost=findViewById(R.id.tabhost);
        tabHost.setup();
        createTab();
    }

    private void createTab() {
        //tab1
        TabHost.TabSpec tabSpec1;
        tabSpec1 =tabHost.newTabSpec("tab1");
        tabSpec1.setContent(R.id.tab1);
        tabSpec1.setIndicator("", getResources().getDrawable(R.drawable.icontrangchu));
        tabHost.addTab(tabSpec1);
        //tab2
        TabHost.TabSpec tabSpec2;
        tabSpec2=tabHost.newTabSpec("tab2");
        tabSpec2.setContent(R.id.tab2);
        tabSpec2.setIndicator("", getResources().getDrawable( R.drawable.icontimkiem));
        tabHost.addTab(tabSpec2);


    }

    private void GetJsonYoutube(String url) {
        Log.e("AAA", url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonItems = response.getJSONArray("items");
                            String title = "";
                            String url = "";
                            String idVideo = "";
                            for (int i = 0; i < jsonItems.length(); i++) {

                                JSONObject jsonItem = jsonItems.getJSONObject(i);

                                JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");

                                title = jsonSnippet.getString("title");

                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                                url = jsonMedium.getString("url");

                                JSONObject jsonResourceID = jsonSnippet.getJSONObject("resourceId");
                                idVideo = jsonResourceID.getString("videoId");

                                arrayVideo.add(new VideoYoutube(title, url, idVideo));
                            }
                            adapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Lỗi!!",
                        Toast.LENGTH_LONG).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);

    }
    private void searchKey(String url1) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItems = response.getJSONArray("items");
                            Log.e("sum", jsonItems.length()+"AAA");

                            for (int i = 0; i < jsonItems.length(); i++) {
                                String title = "";
                                String url = "";
                                String idVideo = "";

                                JSONObject jsonItem = jsonItems.getJSONObject(i);

                                JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");

                                title = jsonSnippet.getString("title");

                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                                url = jsonMedium.getString("url");

                                JSONObject jsonResourceID = jsonItem.getJSONObject("id");
                                idVideo = jsonResourceID.getString("videoId");

                                arraySearchList.add(new VideoYoutube(title, url, idVideo));
                                Log.e("AAA", idVideo);
                            }
                            searchAdapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Lỗi!!",
                        Toast.LENGTH_LONG).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);

    }
}