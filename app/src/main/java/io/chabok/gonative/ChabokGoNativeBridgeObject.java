package io.chabok.gonative;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.adpdigital.push.AdpPushClient;
import com.adpdigital.push.ChabokEvent;

import org.json.JSONException;
import org.json.JSONObject;

public class ChabokGoNativeBridgeObject {

    public Context mContext = null;

    public ChabokGoNativeBridgeObject(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public void login(String userId) {
        AdpPushClient.get().login(userId);
    }

    @JavascriptInterface
    public void logout() { AdpPushClient.get().logout(); }

    @JavascriptInterface
    public void track(String eventName) {
        AdpPushClient.get().track(eventName);
    }

    @JavascriptInterface
    public void track(String eventName, String dataStr) {
        try {
            JSONObject jsonData = new JSONObject(dataStr);
            AdpPushClient.get().track(eventName, jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void trackPurchase(String eventName, String dataStr) {
        try {
            JSONObject jsonData = new JSONObject(dataStr);
            double revenue = jsonData.getDouble("revenue");

            ChabokEvent chabokEvent = new ChabokEvent(revenue);
            if (jsonData.has("currency")) {
                String currency = jsonData.getString("Currency");
                chabokEvent.setRevenue(revenue, currency);
            }
            if (jsonData.has("data")) {
                JSONObject data = jsonData.getJSONObject("data");
                chabokEvent.setData(data);
            }

            AdpPushClient.get().trackPurchase(eventName, chabokEvent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
