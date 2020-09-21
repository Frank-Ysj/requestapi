package com.ysjtest.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import net.sf.json.JSONSerializer;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * Hello world!Test
 *
 */
public class App 
{
    public static void main( String[] args )throws Exception
    {
        String getResult=sendGet("http://localhost:5000/api/User/getget","");
        System.out.println( "Get结果："+getResult );
        User user=new User();
        user.setPhone("18463965388");
        user.setNickname("Hello");
        user.setPassword("romens");
        user.setState(1);
        user.setSign("sign");
        System.out.println(user.toString());
        String postResult=sendPostRequest(JSONSerializer.toJSON(user).toString());
        System.out.println( "Post结果："+postResult );
    }

    public static void sendMeg() throws ApiException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Long timestamp = System.currentTimeMillis();
        String sigh=getSign(timestamp);
        String url="https://oapi.dingtalk.com/robot/send?access_token=40e5f75792ef7b934b92cfb3a9c065d2456635c149a101fed022bc7cab3e00f4";
        url+="&timestamp="+timestamp+"&sign="+sigh;
        DingTalkClient client = new DefaultDingTalkClient(url);
        //&timestamp={timestamp}&sign={sign}
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("测试文本消息，天气依然很好");
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList("18463965388"));
// isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(false);
        request.setAt(at);


//        request.setMsgtype("link");
//        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
//        link.setMessageUrl("https://www.dingtalk.com/");
//        link.setPicUrl("");
//        link.setTitle("时代的火车向前开");
//        link.setText("这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
//        request.setLink(link);
//
//        request.setMsgtype("markdown");
//        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
//        markdown.setTitle("杭州天气");
//        markdown.setText("#### 杭州天气 @156xxxx8827\n" +
//                "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
//                "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n"  +
//                "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n");
//        request.setMarkdown(markdown);
        OapiRobotSendResponse response = client.execute(request);
        System.out.println( response.getBody() );
    }

    public static String getSign(Long timestamp) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String secret = "SECcaa0c6ee22198085940e95f958760721f64af87946a0511ed1b752605e0ecaf4";
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        return sign;

    }

    public static String sendPostRequest(String data) {
        //请求路径
        String inputHost = "http://127.0.0.1:5000/api/user/CreateUser";
        //请求报文
        String inputMessage = data.isEmpty()?"{\n" +
                "\t\"phone\": \"18463965388\",\n" +
                "\t\"password\": \"romens\",\n" +
                "\t\"nickname\": \"hello\",\n" +
                "\t\"state\": 1,\n" +
                "\t\"sign\": \"\"\n" +
                "}":data;
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        HttpHeaders headers = new HttpHeaders();
        //设置请求发送方式
        HttpMethod method = HttpMethod.POST;
        //以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        headers.setAccept(list);
        //token修改下
        headers.set("Authorization", "token");
        //将请求头部和参数合成一个请求
        HttpEntity<String> requestEntity = new HttpEntity<>(inputMessage, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化（可设置为对应返回值格式的类）
        ResponseEntity<String> response = client.exchange(inputHost, method, requestEntity, String.class);
        //返回报文
        String outputMessage = response.getBody();
        return outputMessage;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url发送请求的URL
     * @param param请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + (param.length() <= 0?"?":"") + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();// 打开和URL之间的连接
            connection.setRequestProperty("accept", "*/*");// 设置通用的请求属性
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();// 建立实际的连接
            Map<String, List<String>> map = connection.getHeaderFields();// 获取所有响应头字段
            for (String key : map.keySet()) {// 遍历所有的响应头字段
                System.out.println(key + "--->" + map.get(key));
            }
            in = new BufferedReader(new InputStreamReader(// 定义 BufferedReader输入流来读取URL的响应
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
