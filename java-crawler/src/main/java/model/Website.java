package model;

import java.util.Objects;

public class Website {

    public Website(String url) {
        this.url = url;
    }
    private String url;
    private int websiteRank;
    private String country;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Website website = (Website) o;
        return Objects.equals(url, website.url);
    }

    public void setWebsiteRank(int websiteRank) {
        this.websiteRank = websiteRank;
    }

    public int getWebsiteRank() {
        return websiteRank;
    }

}
