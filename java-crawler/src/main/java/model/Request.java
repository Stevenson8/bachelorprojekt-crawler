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
    private Optional<String> redirectedPage;
    private EInternetProtocol protocol;

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
}
