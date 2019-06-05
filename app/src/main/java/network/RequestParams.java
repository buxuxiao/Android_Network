package network;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hulifei on 2018/8/29.
 */

public class RequestParams extends HashMap<String, String> {

    public void addBodyParameter(String key, String value) {
        put(key, value);
    }
}
