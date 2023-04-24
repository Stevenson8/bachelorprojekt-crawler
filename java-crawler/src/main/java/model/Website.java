package model;

import java.util.Objects;

public class Website {

    public Website(String url) {
        this.url = url;
    }
    private String url;
    private String country;

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Website website = (Website) o;
        return Objects.equals(url, website.url);
    }

}
