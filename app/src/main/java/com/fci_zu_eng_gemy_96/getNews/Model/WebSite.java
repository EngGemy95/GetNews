package com.fci_zu_eng_gemy_96.getNews.Model;

import java.util.List;

public class WebSite {
    private String status ;
    private List<Sources> sources = null ;

    public WebSite() {
    }

    public WebSite(String status, List<Sources> sourceList) {
        this.status = status;
        this.sources = sourceList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Sources> getSourceList() {
        return sources;
    }

    public void setSourceList(List<Sources> sourceList) {
        this.sources = sourceList;
    }

}
