package com.example.ltdd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

public class PlayVideoActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {
    String id = "";
    ImageView btnLike;
    Button button;
    YouTubePlayerView youTubePlayerView;
    int REQUEST_VIDEO= 12;
    VideoYoutube item;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        youTubePlayerView =findViewById(R.id.myYoutube);
        dataBase  = new DataBase(PlayVideoActivity.this, "database.sqlite", null, 1);
        btnLike =findViewById(R.id.btnLike);
        button=findViewById(R.id.button);
        item  = (VideoYoutube) getIntent().getSerializableExtra("item");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(PlayVideoActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.dialog_like_list);

                ArrayList<VideoYoutube> arrayVideo = new ArrayList<>();
                VideoYouTubeAdapter adapter = new VideoYouTubeAdapter(PlayVideoActivity.this, R.layout.row_video_youtube, arrayVideo);

                ImageButton btnClose = dialog.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ListView listView = dialog.findViewById(R.id.list);
                listView.setAdapter(adapter);
                Cursor cursor = dataBase.GetData("SELECT * FROM like");
                while (cursor.moveToNext()){
                    arrayVideo.add(new VideoYoutube(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent =new Intent(PlayVideoActivity.this, PlayVideoActivity.class);
                        intent.putExtra("idVideoYoutube",arrayVideo.get(position).getIdVideo());
                        intent.putExtra("item", arrayVideo.get(position));
                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();
                dialog.show();
            }
        });
        Cursor cursor = dataBase.GetData("SELECT * FROM like WHERE idVideo = '"+ item.getIdVideo()+"'");
        if(cursor.getCount() >0){
            btnLike.setColorFilter(Color.argb(255, 255, 0, 0));
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dataBase.GetData("SELECT * FROM like WHERE idVideo = '"+ item.getIdVideo()+"'");
                String s;
                if(cursor.getCount() >0){
                    s = "DELETE FROM like WHERE idVideo = '"+ item.getIdVideo()+"'";
                    dataBase.QueryData(s);
                    Toast.makeText(PlayVideoActivity.this, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                    btnLike.setColorFilter(Color.argb(255, 0, 0, 0));
                }
                else {
                    s ="INSERT INTO like ('title', 'thumbnail', 'idVideo')  " +
                            "VALUES ('" + item.getTitle() +
                            "', '" + item.getThumbnail() +
                            "', '" + item.getIdVideo()+"')";
                    dataBase.QueryData(s);
                    Toast.makeText(PlayVideoActivity.this, "Đã thêm vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                    btnLike.setColorFilter(Color.argb(255, 255, 0, 0));
                }


            }
        });


        Intent intent=getIntent();
        id = intent.getStringExtra("idVideoYoutube");
        youTubePlayerView.initialize(MainActivity.API_KEY, this);


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo(id);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            if (youTubeInitializationResult.isUserRecoverableError()){
                youTubeInitializationResult.getErrorDialog(PlayVideoActivity.this, REQUEST_VIDEO);
            }
            else {
                Toast.makeText(this, "Lỗi!!", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_VIDEO){
            youTubePlayerView.initialize(MainActivity.API_KEY,PlayVideoActivity.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}