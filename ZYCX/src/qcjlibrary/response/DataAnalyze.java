package qcjlibrary.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import qcjlibrary.model.ModelMsg;
import qcjlibrary.util.JsonUtils;

import com.google.gson.Gson;

import android.util.Log;

/**
 * author：qiuchunjia time：下午2:44:40
 * 
 * 类描述：这个类是实现请求的返回的数据解析
 *
 */

public class DataAnalyze {
	public static final String DATA = "data";
	public static final String CODE = "code"; // 识别码

	/**
	 * 该方法通过传递进来的类型，来解析相应的数据对象，当为数组就解析为数据对象，为单个的就是解析为单个的
	 * 
	 * @param str
	 *            网络数据
	 * @param class1
	 *            需要解析的数据类型
	 * @return
	 */
	public static Object parseData(String str, Class classType) {

		if (str != null) {
			Log.i("parseData", str);
			try {
				JSONObject jsonObject = new JSONObject(str);
				// 如果只想想获取modelmsg类型就直接返回
				if (classType.equals(ModelMsg.class)) {
					return JsonUtils
							.parseJsonObject(jsonObject, ModelMsg.class);
				}
				if (jsonObject.has(CODE)) {
					int code = jsonObject.getInt(CODE);
					// 如果code==0就表示正常的反馈就可以获取数据了，不然的话就还是获取modelmsg类型
					if (code != 0) {
						// TODO 获取modelmsg
						return JsonUtils.parseJsonObject(jsonObject,
								ModelMsg.class);
					} else {
						// TODO 获取数据
						if (jsonObject.has(DATA)) {
							String result = jsonObject.getString(DATA);
							// 根据情况转为jsonarray或者jsonobject;
							if (result.indexOf("[") == 0) {
								JSONArray dataArray = jsonObject
										.getJSONArray(DATA);
								return JsonUtils.parseJsonArray(dataArray,
										classType);
							} else {
								return JsonUtils.parseJsonObject(
										jsonObject.getJSONObject(DATA),
										classType);
							}
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 通过Gson来解析json
	 * 
	 * @param str
	 * @param class1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object parseDataByGson(String str, Class classType) {
		if (str != null) {
			Log.i("parseDataByGson", str);
			try {
				JSONObject jsonObject = new JSONObject(str);
				// 如果只想想获取modelmsg类型就直接返回
				if (classType.equals(ModelMsg.class)) {
					Gson gson = new Gson();
					return gson.fromJson(jsonObject.toString(), classType);
				}
				if (jsonObject.has(CODE)) {
					int code = jsonObject.getInt(CODE);
					// 如果code==0就表示正常的反馈就可以获取数据了，不然的话就还是获取modelmsg类型
					Gson dataGson = new Gson();
					if (code != 0) {
						// TODO 获取modelmsg
						return dataGson.fromJson(jsonObject.toString(),
								ModelMsg.class);
					} else {
						// TODO 获取数据
						if (jsonObject.has(DATA)) {
							String result = jsonObject.getString(DATA);
							return dataGson.fromJson(result, classType);
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
