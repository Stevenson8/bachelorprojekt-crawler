package model;

import com.sun.jdi.PrimitiveValue;

import java.util.List;
import java.util.Optional;

public class Request {
    private ERequestStatus requestStatus;
    private List<Cookie> cookies;
    private ERegion originRegion;
    private boolean wasRedirected;
    private Optional<String> redirectedPage;
    private EInternetProtocol protocol;
}
