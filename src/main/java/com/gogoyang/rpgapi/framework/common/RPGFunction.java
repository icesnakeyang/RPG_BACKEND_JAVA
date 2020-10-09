package com.gogoyang.rpgapi.framework.common;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.framework.outData.IOutDataBox;
import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import com.gogoyang.rpgapi.meta.sms.service.ISMSLogService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RPGFunction implements IRPGFunction {
    private final ISMSLogService ismsLogService;
    private final IOutDataBox iOutDataBox;

    /**
     * 文件服务器域名
     */
    @Value("${oss.domain}")
    private String domainName;

    @Value("${oss.imagesPath}")
    private String theSetDir; //全局配置文件中设置的图片的路径

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.gogobuket}")
    private String gogobuket;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public RPGFunction(ISMSLogService ismsLogService,
                       IOutDataBox iOutDataBox) {
        this.ismsLogService = ismsLogService;
        this.iOutDataBox = iOutDataBox;
    }

    /**
     * 把传入的字符串进行MD5加密后，返回生成的MD5码
     *
     * @param string
     * @return
     * @throws Exception
     */
    @Override
    public String encoderByMd5(String string) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] newpass = Base64.encodeBase64(md5.digest(string.getBytes("utf-8")));
        String str = new String(newpass);

        return str;
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @throws Exception
     */
    @Override
    public void sendMSM(String phone, String codeStr) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "0O9R0XlXcfYcPUwB", "Vhx4wv8LUrpkQkeLQ0BYKMPg9KV950");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "GOGORPG");
        request.putQueryParameter("TemplateCode", "SMS_177544037");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + codeStr + "\"}");
        CommonResponse response = client.getCommonResponse(request);
        String result = response.getData().substring(response.getData().length() - 4, response.getData().length() - 2);
        if (!result.equals("OK")) {
            throw new Exception("10106");
        }
        logger.info(response.getData());
    }

    @Override
    public void verifyMSMCode(String phone, String code) throws Exception {
        SMSLog smsLog = ismsLogService.getSMSLog(phone, code);
        if (smsLog == null) {
            throw new Exception("10108");
        }

        if (!smsLog.getStatus().equals(LogStatus.WAITING.toString())) {
            throw new Exception("10108");
        }

        /**
         * 计算当前时间是否已经超过15分钟
         */
        Date now = new Date();
        long currentTime = now.getTime();
        long theTime = smsLog.getCreateTime().getTime();

        long diff = (currentTime - theTime) / 1000 / 60;

        if (diff > 15) {修改
            throw new Exception("30012");
        }
    }

    public String convertMapToString(HashMap map) throws Exception {
        Iterator iter = map.entrySet().iterator();
        String outStr = "";
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            outStr += key.toString() + ":";
            Object val = entry.getValue();
            if (val != null) {
                outStr += val.toString() + "/";
            } else {
                outStr += "null/";
            }
        }
        return outStr;
    }

    public UUID UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    /**
     * 判断字符串是否为email
     *
     * @param email
     * @return
     */
    @Override
    public boolean checkEmail(String email) throws Exception {
        if (null == email || "".equals(email)) return false;
//        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    public Map GenerateImage(String imgStr, Integer fileIndex) throws Exception {
        //对字节数组字符串进行Base64解码并生成图片
        //Base64解码
        byte[] b = Base64.decodeBase64(imgStr);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {//调整异常数据
                b[i] += 256;
            }
        }
        //生成jpeg图片
        String fileName = "detailpics" + GogoTools.UUID() + fileIndex + ".jpg";//新生成的图片
        String ossPath = theSetDir;
        fileName = ossPath + "/" + fileName;

        String endpoint = domainName;

        /**
         * todo
         * 在时空笔记上开发
         * 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，
         * 请登录 https://ram.console.aliyun.com 创建。
         *
         */
        // 创建OSSClient实例。
        Map boxIn = new HashMap();
//        Map boxOut=iOutDataBox.getOutData(boxIn);
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        //创建缓存文件
        File f = null;
        f = File.createTempFile("tmp", null);
        InputStream sbs = new ByteArrayInputStream(b);
        ossClient.putObject(gogobuket, fileName, sbs);

        // 关闭OSSClient。
        ossClient.shutdown();
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(gogobuket, fileName, expiration).toString();
        f.deleteOnExit();

        Map out = new HashMap();
        out.put("url", url);
        out.put("fileName", fileName);

        return out;
    }

    @Override
    public Map processRichTextPics(String detail) throws Exception {
        Integer index = detail.indexOf("data:image/png;base64,");
        Integer fileIndex = 0;
        ArrayList urlList = new ArrayList();
        ArrayList fileList = new ArrayList();
        Map out = new HashMap();
        while (index != -1) {
            index += 22;
            Integer end = detail.indexOf('"', index);
            String imgstr = detail.substring(index, end);
            fileIndex++;
            Map map = GenerateImage(imgstr, fileIndex);
            //这里要把url和detail里的img置换
            String url = map.get("url").toString();
            String s1 = "data:image/png;base64,";
            s1 += imgstr;
            detail = detail.replace(s1, url);
            index = detail.indexOf("data:image/png;base64,", index);
            fileList.add(map);
        }
        out.put("detail", detail);
        out.put("fileList", fileList);
        return out;
    }

    /**
     * 删除oss服务器的文件
     *
     * @param fileName
     * @throws Exception
     */
    @Override
    public void deleteOSSFile(String fileName) throws Exception {
        String endpoint = domainName;
        // 创建OSSClient实例。

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(gogobuket, fileName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
