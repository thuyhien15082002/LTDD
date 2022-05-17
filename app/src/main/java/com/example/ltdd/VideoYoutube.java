package com.example.ltdd;

import java.io.Serializable;

public class VideoYoutube implements Serializable {
    private String Title;
    private String Thumbnail;
    private String IdVideo;





    public VideoYoutube(String title, String thumbnail, String idVideo) {
        Title = title;
        Thumbnail = thumbnail;
        IdVideo = idVideo;

    }


    public String getTitle() {
        return Title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getIdVideo() {
        return IdVideo;
    }

    public void setIdVideo(String idVideo) {
        IdVideo = idVideo;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
