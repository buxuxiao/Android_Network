package network;

import android.util.Base64;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.Headers;

public class Session {
    public static String BASE_URL = "http://122.152.212.182:9001";
    public static String HmacSecret = null;
    public static String userId = null;
    public static String sessionId = null;
    public static String langId = null;
    public static String orgNo = null;
    public static String skin = null;

    public static void login(String usrId, String pass) {
        userId = usrId.toUpperCase();
        pass = md5(pass);
        final String finalPass = pass;
        getSalt(new CallBack() {
            @Override
            public void rendView(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String ret = jsonObject.optString("result");
                        if ("success".equals(ret)) {
                            String salt = jsonObject.optString("value");
                            setSalt(salt, finalPass);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static void setSalt(String salt, String pass) {
        HmacSecret = pbkdf2(pass, salt, 1000, 256);
        login(new CallBack() {
            @Override
            public void rendView(String result) {
                setLogin(result);
            }
        });
    }


    private static void setLogin(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ret = jsonObject.optString("result");
        if ("success".equals(ret)) {
            jsonObject = jsonObject.optJSONObject("jwt");
            String key = jsonObject.optString("key");
            String aesKey = md5(HmacSecret).substring(8, 24);
            byte[] data = AESDecrypt(Base64.decode(key, Base64.DEFAULT), aesKey.getBytes());
            HmacSecret=new String(data);
            //sessionId = jsonObject.optString("sessionId");
        }
    }

    public static void request() {
        request(new CallBack() {
            @Override
            public void rendView(String result) {
                Log.d("abc", result);
            }
        });
    }

    public static void logout() {
        logout(new CallBack() {
            @Override
            public void rendView(String result) {
            }
        });
    }


    private static void getSalt(CallBack callback) {
        HttpUtil util = new HttpUtil();
        String action = "/Liems/webservice/getSalt";
        RequestParams params = new RequestParams();
        params.addBodyParameter("usrId", userId);
        util.send(BASE_URL + action, params, callback);
    }

    private static void login(CallBack callback) {
        HttpUtil util = new HttpUtil();
        String action = "/Liems/webservice/login";
        RequestParams params = new RequestParams();
        sessionId = null;
        params.addBodyParameter("jwt", getJwt());
        util.send(BASE_URL + action, params, callback);
    }

    private static void request(CallBack callback) {
        HttpUtil util = new HttpUtil();
        String action = "/Liems/webservice/orginfo";
        RequestParams params = new RequestParams();
        params.addBodyParameter("userid", "SYS");
        Headers headers = new Headers.Builder()
                .add("Authorization", getJwt())
                .add("version", "7.0")
                .build();
        util.send(BASE_URL + action, headers, params, callback);
    }

    private static void logout(CallBack callback) {
        HttpUtil util = new HttpUtil();
        String action = "/Liems/service/user/logout";
        RequestParams params = new RequestParams();
        params.addBodyParameter("jwt", getJwt());
        util.send(BASE_URL + action, params, callback);
    }

    private static String getJwt() {
        Map payload = new HashMap();
        long jti = new Date().getTime();
        long iat = jti / 1000;
        long exp = iat + 5 * 60;
        try {
           if(sessionId==null){
               sessionId=String.valueOf(jti);
           }

            payload.put("sub", userId);
            payload.put("exp", exp);
            payload.put("iat", iat);
            payload.put("iss", "PC000001");
            payload.put("jti", jti);
            payload.put("sessionId",sessionId);
            payload.put("langId", langId == null ? "" : langId);
            payload.put("orgNo", orgNo == null ? "" : orgNo);
            payload.put("skin", skin == null ? "" : skin);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String jwt = null;
        try {
            jwt = Jwts.builder()
                    .setClaims(payload)
                    .signWith(SignatureAlgorithm.HS256, HmacSecret.getBytes("UTF-8"))
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jwt;
    }

    private static String md5(String message) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(message.getBytes());
            return byte2Hex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String pbkdf2(String password, String salt, int iterationCount, int keyLength) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return byte2Hex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] AESDecrypt(byte[] message, byte[] secret) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, "AES"));
            return cipher.doFinal(message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String stmp;
        for (int i = 0; i < bytes.length; i++) {
            stmp = Integer.toHexString(bytes[i] & 0xff);
            if (stmp.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(stmp);
        }
        return stringBuilder.toString().toLowerCase();
    }

    public static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }
}


