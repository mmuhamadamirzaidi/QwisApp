package com.mmuhamadamirzaidi.qwisapp.Model;

public class Post {

    String titlepost;
    String descpost;
    String keypost;

    public Post() {
    }

    public Post(String titlepost, String descpost, String keypost) {
        this.titlepost = titlepost;
        this.descpost = descpost;
        this.keypost = keypost;
    }

    public String getTitlepost() {
        return titlepost;
    }

    public void setTitlepost(String titlepost) {
        this.titlepost = titlepost;
    }

    public String getDescpost() {
        return descpost;
    }

    public void setDescpost(String descpost) {
        this.descpost = descpost;
    }

    public String getKeypost() {
        return keypost;
    }

    public void setKeypost(String keypost) {
        this.keypost = keypost;
    }
}
