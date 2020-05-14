package com.fci_zu_eng_gemy_96.getNews.Model;

public class Icon {
    private String url ;
    private int width , height , bytes ;
    private String format , sh1sum ;
    private Object error ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSh1sum() {
        return sh1sum;
    }

    public void setSh1sum(String sh1sum) {
        this.sh1sum = sh1sum;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
