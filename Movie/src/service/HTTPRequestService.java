package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONObject;

public class HTTPRequestService {
  
  /**
   * 发送Get请求，返回JSONObject
   * @param url
   * @return JSONObject
   */
  public JSONObject doGet(String url) {
    String resultStr = "";
    // BufferedReader in = null;
    try {
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      URLConnection connection = realUrl.openConnection();
      // 建立实际的连接
      connection.connect();
      // 定义 BufferedReader输入流来读取URL的响应
      BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
        resultStr += line;
      }
      JSONObject result = JSONObject.parseObject(resultStr);
      return result;
    } catch (Exception e) {
//      System.out.println("发送GET请求出现异常！" + e);
//      e.printStackTrace();
      return null;
    }
  }
}
