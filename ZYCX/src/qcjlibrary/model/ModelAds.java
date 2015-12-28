package qcjlibrary.model;

import org.json.JSONException;
import org.json.JSONObject;

import qcjlibrary.model.base.Model;

/**
 * Created by Administrator on 2015/12/28.
 */
public class ModelAds extends Model {
    //    "banner": "7834",
//            "bannerurl": "type=news&id=1",
//            "bannerpic": "http://qingko-img.b0.upaiyun.com/2015/1113/11/5645525ca7f78.jpg",
//            "parameters": {
//        "type": "news",
//                "id": "1"
//}
    private String banner;
    private String bannerurl;
    private String bannerpic;
//    private String parameters;

    public ModelAds() {

    }

    public ModelAds(JSONObject data) {
        try {
            if (data.has("banner")) {

                setBanner(data.getString("banner"));
            }
            if (data.has("bannerurl")) {

                setBannerurl(data.getString("bannerurl"));
            }
            if (data.has("bannerpic")) {

                setBannerpic(data.getString("bannerpic"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl;
    }

    public String getBannerpic() {
        return bannerpic;
    }

    public void setBannerpic(String bannerpic) {
        this.bannerpic = bannerpic;
    }
}
