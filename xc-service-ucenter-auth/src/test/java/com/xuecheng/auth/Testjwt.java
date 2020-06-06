package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author study
 * @create 2020-04-24 16:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Testjwt {

    //创建jwt令牌
    @Test
    public void testCreateJwt(){
        //秘钥库文件
        String key_location="my.keystore";
        //秘钥库的密码
        String keystore_password="123456store";
        //访问证书路径
        ClassPathResource resource=new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,keystore_password.toCharArray());
        //密钥的钥匙，此密码和别名要匹配
        String keypassword="123456";
        //密匙别名
        String alias="mykey";
        //密匙对(密匙和公匙)
        KeyPair keyPair=keyStoreKeyFactory.getKeyPair(alias,keypassword.toCharArray());
        //私匙
        RSAPrivateKey rsaPrivateKey= (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id","123");
        map.put("name","mrt");
        map.put("roles","r01,r02");
        map.put("ext",1);
        //生成jwt令牌
        Jwt jwt= JwtHelper.encode(JSON.toJSONString(map),new RsaSigner(rsaPrivateKey));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token="+token);

    }

    //验证jwt令牌
    @Test
    public void testVerify(){
        //jwt令牌
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1ODg1MzQxMDYsImF1dGhvcml0aWVzIjpbImNvdXJzZV9nZXRfYmFzZWluZm8iLCJjb3Vyc2VfcGljX2xpc3QiXSwianRpIjoiMmJiYzMyZDQtOTVjZS00NWFmLTlmOWQtYTRmYzZhZjBlOGZjIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.f2UKQYrhhXoT8rHKi0gjg5PLmjUKe4kfpiAhsDYdt4Uuq17P14rMvMyk0KCVhKXdvPkCOAqtfrxYlMYpYSZi-Fr9FSJFsv8TYHtCrfmqwtrBu0gr2VDGlN4VF1kIA1vkW4S1N9Gmxfdjx4xj9YFuz7DYPuWbRVDZ718-0sB-hresXWyrIvhqkkoxyqeW-SCAWni6MZwkhpaD8yWj3XqgCg9G2kgb4_SekGzw_-D0zhrQC_ren2skbI-IOAfu1MJnjY-ZwFdiz5-5A5I9cCwuPbEVyGJNswfxahtq7d_JL1A1lgG7OHGI8UY7F9-8rDo7Fl-NJWZX4aY1PHVK4ymrWw";
        //公匙
        String publicKey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        //校验jwt
        Jwt jwt=JwtHelper.decodeAndVerify(token,new RsaVerifier(publicKey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
