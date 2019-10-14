/**
 * @filename SpeechDemo.java
 * @author lg
 * @date 2019年3月28日 下午4:36:40
 * @version 1.0
 * Copyright (C) 2019 
 */

package com.unique;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;

public class SpeechDemo {
    //设置APPID/AK/SK
    public static final String APP_ID = "15871052";
    public static final String API_KEY = "tqDlru9AERM5USZGQTkle6Xu";
    public static final String SECRET_KEY = "Wp51Aa8Fl6TbgXNmdNtZAk8EpSw6428C";

    public static void main(String[] args) throws Exception {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(5000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
      //  System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

      //  mp3Convertpcm("D:/speech/1.mp3","D:/speech/1.pcm");
        // 对本地语音文件进行识别
         String path = "D:/speech/unique.wav";
//        JSONObject asrRes = client.asr(path, "pcm", 16000, null);
//        System.out.println(asrRes.toString(2));

        // 对语音二进制数据进行识别
        byte[] data = Util.readFileByBytes(path);     //readFileByBytes仅为获取二进制数据示例
        JSONObject asrRes2 = client.asr(data, "wav", 16000, null);
        System.out.println(asrRes2.toString(2));
    }
    
    /*sdk语音合成目前支持mp3、wav、pcm格式，api支持其他格式，但是语音识别目前又不支持mp3，所以要进行格式转换
     * 使用ffmpeg工具
     * ffmpeg -y  -i output.mp3  -acodec pcm_s16le -f s16le -ac 1 -ar 16000 output.pcm 
     * */
    public static void main2(String[] args) throws IOException {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech("15871052", "tqDlru9AERM5USZGQTkle6Xu", "Wp51Aa8Fl6TbgXNmdNtZAk8EpSw6428C");
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(5000);
//        client.setSocketTimeoutInMillis(60000);
        
     // 设置可选参数
    	HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5");
        options.put("pit", "5");
        options.put("per", "0");
        options.put("aue", "4");
        // 调用接口
        TtsResponse res = client.synthesis("你好刘广", "zh", 1, options);
        
        JSONObject result = res.getResult();    //服务器返回的内容，合成成功时为null,失败时包含error_no等信息
        byte[] data = res.getData();  
        System.out.println(res.getResult());
        System.out.println(data.length);
        if (data != null) {//生成的音频数据
            try {
                Util.writeBytesToFileSystem(data, "4.pcm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (result != null) {
            System.out.println(result.toString(2));
        }
       
        
    }
    
    
    public static void main3(String[] args) {
    	 AipSpeech aipSpeech = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		 HashMap<String, Object> options = new HashMap<String, Object>();
		    options.put("spd", "5");
		    options.put("pit", "5");
		    options.put("per", "4");
		 TtsResponse ttsResponse = aipSpeech.synthesis("你好小帅", "zh", 1, options);
		 byte[] aa = ttsResponse.getData();
		 getFile(aa, "D:/speech/", "1.mp3");
		 System.out.println(ttsResponse.getResult());
	     System.out.println(aa.length);
	}
	public static void getFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "\\" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	
}

