package com.gogoyang.rpgapi.framework.outData;

import com.alibaba.fastjson.JSONObject;
import com.gogoyang.rpgapi.framework.common.GogoTools;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OutDataBox implements IOutDataBox{
    private final RestTemplate restTemplate;

    public OutDataBox(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map getOutData(Map in) throws Exception{

        //获取一个公钥
        Map out=getRSAPublicKey(in);
        String publicKey=out.get("publicKey").toString();
        String keyToken=out.get("keyToken").toString();

        String url = "http://localhost:8088/userData/getUserDataApi";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestHeaders.add("token", "dcfbc543-d549-467f-8e9c-de09c5038b78");
        JSONObject postData = new JSONObject();
//        postData.put("token", "dcfbc543-d549-467f-8e9c-de09c5038b78");
        postData.put("dataToken","1cdc5a93-c566-4c86-809f-d9816ddecd12");
        postData.put("keyToken",keyToken);

        //生成一个AES秘钥
        String AESKey=GogoTools.generateAESKey128();
        System.out.println("AES:"+AESKey);
        //用RSA公钥加密AES秘钥

        String codec=GogoTools.RSAPublicKeyEncrypt(AESKey, publicKey);
        postData.put("encryptKey", codec);

        HttpEntity<String> formEntity = new HttpEntity<String>(postData.toString(), requestHeaders);
        JSONObject json=restTemplate.postForEntity(url, formEntity, JSONObject.class).getBody();
        out=new HashMap();
        String codeStr=json.get("code").toString();
        if(codeStr.equals("10001")){
            throw new Exception("30020");
        }
        Map dataMap=(Map)json.get("data");
        Map noteMap=(Map)dataMap.get("note");

        //用AES秘钥解密笔记的AES秘钥
        String userEncodeKey=noteMap.get("userEncodeKey").toString();
        String noteKey=GogoTools.decryptAESKey(userEncodeKey, AESKey);

        String detail=GogoTools.decryptAESKey256(noteMap.get("detail").toString(), noteKey);

        out.put("response", json);

        return out;
    }

    private Map getRSAPublicKey(Map in) throws Exception{
        ResponseEntity<String> res = restTemplate.getForEntity(

                "http://localhost:8088/security/requestRSAPublicKey",

                String.class);

        JSONObject obj=JSONObject.parseObject(res.getBody());
        Map out=new HashMap();
        String publicKey=((Map)obj.get("data")).get("publicKey").toString();
        String keyToken=((Map)obj.get("data")).get("keyToken").toString();
        out.put("publicKey", publicKey);
        out.put("keyToken", keyToken);
        return out;
    }
}
