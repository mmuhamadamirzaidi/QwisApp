package com.mmuhamadamirzaidi.qwisapp.Model;

public class Post {

    String titlepost;
    String descdpost;
    String keypost;

    public Post() {
    }

    public Post(String titlepost, String descdpost, String keypost) {
        this.titlepost = titlepost;
        this.descdpost = descdpost;
        this.keypost = keypost;
    }

    public String getTitlepost() {
        return titlepost;
    }

    public void setTitlepost(String titlepost) {
        this.titlepost = titlepost;
    }

    public String getDescdpost() {
        return descdpost;
    }

    public void setDescdpost(String descdpost) {
        this.descdpost = descdpost;
    }

    public String getKeypost() {
        return keypost;
    }

    public void setKeypost(String keypost) {
        this.keypost = keypost;
    }
}
