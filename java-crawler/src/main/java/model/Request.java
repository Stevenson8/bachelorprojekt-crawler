package model;

import com.sun.jdi.PrimitiveValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Request {
    private ERegion originRegion;
    private ERequestStatus requestStatus;
    private List<Cookie> cookies;
    private boolean wasRedirected;
    private String redirectedPage;
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

    public void setRedirectedPage(String redirectedPage) {
        this.redirectedPage = redirectedPage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ERequestStatus getRequestStatus() {
        return requestStatus;
    }

    public boolean getWasRedirected() {
        return wasRedirected;
    }

    public String getRedirectedPage() {
        return redirectedPage;
    }

    public EInternetProtocol getProtocol() {
        return protocol;
    }

    public String getDate() {
        return date;
    }
}
