package com.example.ltdd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchMainActivity extends AppCompatActivity {

    EditText editText;
    ImageButton imageButton2;

    String url = "https://developers.google.com/"+getString(R.string.API_key)+"/#p/youtube/v3/youtube.search.list?" +
            "part=snippet" +
            "&order=viewCount" +
            "&q=";
    String duoi="&type=video" +
            "&videoDefinition=high";
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
Log.e("AAA", "po");
        editText = findViewById(R.id.editText);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = editText.getText().toString();
                String link = url + key +duoi;
                Log.e("AAA", link);
            }
        });
    }

}