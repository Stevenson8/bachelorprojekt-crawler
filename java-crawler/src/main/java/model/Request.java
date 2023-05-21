package model;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private ERegion originRegion;
    private ERequestStatus requestStatus;
    private List<Cookie> cookies;
    private String originalUrl;
    private boolean wasRedirected;
    private String redirectedUrl;
    private EInternetProtocol protocol;
    private String date;

    public Request(ERegion originRegion) {
        this.originRegion=originRegion;
        this.cookies=new ArrayList<>();
    }
    public ERegion getOriginRegion() {
        return originRegion;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void addCookie(Cookie cookie){
        cookies.add(cookie);
    }

    public void setProtocol(EInternetProtocol protocol) {
        this.protocol = protocol;
    }

    public void setRequestStatus(ERequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public void setWasRedirected(boolean wasRedirected) {
        this.wasRedirected = wasRedirected;
    }

    public void setRedirectedUrl(String redirectedUrl) {
        this.redirectedUrl = redirectedUrl;
    }
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ERequestStatus getRequestStatus() {
        return requestStatus;
    }

    public boolean wasRedirected() {
        return wasRedirected;
    }

    public String getRedirectedUrl() {
        return redirectedUrl;
    }

    public EInternetProtocol getProtocol() {
        return protocol;
    }

    public String getDate() {
        return date;
    }
    public String getOriginalUrl() {
        return originalUrl;
    }
}
