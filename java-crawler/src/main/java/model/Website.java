package model;

import java.util.Objects;

public class Website {

    public Website(String url) {
        this.baseUrl = url;
    }
    private String baseUrl;
    private int websiteRank;
    private String country;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Website website = (Website) o;
        return Objects.equals(baseUrl, website.baseUrl);
    }

    public void setWebsiteRank(int websiteRank) {
        this.websiteRank = websiteRank;
    }

    public int getWebsiteRank() {
        return websiteRank;
    }

}
