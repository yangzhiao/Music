package cn.com.netEase.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2017/3/24.
 */
@Controller
@RequestMapping("/netEase")
public class NetEaseMusicAPI {
    private static Logger logger = LoggerFactory.getLogger(NetEaseMusicAPI.class);
    private static final String UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.75 Safari/537.36";
    private static String COOKIE="appver=1.5.0.75771;";

    /**
     *
     * @param param 模拟请求参数
     * @param url   请求地址
     * @param referrerUrl 模拟发送地址
     * @return
     * @throws IOException
     */
    public static String cUrl(Map param, String url, String referrerUrl) throws IOException {
        String content = Jsoup.connect(url)
                .data(param)
                .userAgent(UA)
                .cookie("auth", "token")
                .referrer("http://h.xiami.com/")
                .timeout(6000)
                .ignoreContentType(true)
                .post().html();
        logger.debug("总结构====" + content);
        Document doc = Jsoup.parse(content);
        Element element = doc.body();
        //logger.debug("body结构==="+element);
        String body = element.html();
        return body;
    }

    /**
     *
     * @param key 搜索关键字
     * @param type 搜索类型 歌曲:1;专辑:10;歌手:100;歌单:1000;用户:1002;mv:1004;歌词:1006;主播电台:1009
     * @return
     * @throws IOException
     */
    @RequestMapping("/search/songs/{key}/{type}")
    @ResponseBody
    public Object seachSongs(@PathVariable String key, @PathVariable Integer type) {
        Map resultMap = new HashMap();
        try {
            String url = "http://music.163.com/api/search/pc";
            Map param = new HashMap();
            param.put("s", key);
            param.put("type", type.toString());
            param.put("offset", "1");
            param.put("limit", "30");
            String body = cUrl(param,url,"http://music.163.com/");
            logger.debug("search===>body内容:"+body);
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("search===>序列化后的内容:" + jsonObject.toJSONString());
            Integer code = jsonObject.getInteger("code");
            if (code == null || code !=200) {
                Object message = jsonObject.get("message");
                logger.error("search===>code的状态：" + code);
                throw new ServerException(message.toString());
            }
            JSONObject result = jsonObject.getJSONObject("result");
            logger.debug("search===>result的类容" + result);
            resultMap.put("result", 1);
            resultMap.put("date", result);
        } catch (Exception e) {
            logger.error("search===>错误消息" + e.getMessage());
            resultMap.put("result", 0);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取歌曲详情
     * @param songId 歌曲id
     * @return
     * @throws IOException
     */
    @RequestMapping("/search/detail/{songId}")
    @ResponseBody
    public Object songDetail(@PathVariable Integer songId) {
        Map resultMap = new HashMap();
        try {
            String url = "http://music.163.com/api/song/detail?id=" + songId + "&ids=[" +songId + "]";
            Map param = new HashMap();
            String body = cUrl(param,url,"http://music.163.com/");
            logger.debug("song/detail===>body内容:"+body);
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("song/detail===>序列化后的内容:" + jsonObject.toJSONString());
            Integer code = jsonObject.getInteger("code");
            if (code == null || code !=200) {
                Object message = jsonObject.get("message");
                logger.error("song/detail===>code的状态：" + code);
                throw new ServerException(message.toString());
            }
            List songs = jsonObject.getJSONArray("songs");
            logger.debug("song/detail===>songs的类容" + songs);
            resultMap.put("result", 1);
            resultMap.put("date", songs);
        } catch (Exception e) {
            logger.error("song/detail===>错误消息" + e.getMessage());
            resultMap.put("result", 0);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    @RequestMapping("/search/lyric/{songId}")
    @ResponseBody
    public Object songLyric(Integer songId) {
        Map resultMap = new HashMap();
        try {
            String url = "http://music.163.com/api/song/lyric?id=" + songId + "&lv=-1&kv=-1&tv=-1";
            Map param = new HashMap();
            String body = cUrl(param,url,"http://music.163.com/");
            logger.debug("song/lyric===>body内容:"+body);
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("song/lyric===>序列化后的内容:" + jsonObject.toJSONString());
            Integer code = jsonObject.getInteger("code");
            if (code == null || code !=200) {
                Object message = jsonObject.get("message");
                logger.error("song/lyric===>code的状态：" + code);
                throw new ServerException(message.toString());
            }
            JSONObject lrc = jsonObject.getJSONObject("lrc");
            logger.debug("song/lyric===>lrc的类容" + lrc);
            resultMap.put("result", 1);
            resultMap.put("date", lrc);
        } catch (Exception e) {
            logger.error("song/lyric===>错误消息" + e.getMessage());
            resultMap.put("result", 0);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    public static void main(String args[]){
        NetEaseMusicAPI API = new NetEaseMusicAPI();
        //API.seachSongs("小幸运",1);
        //API.songDetail(409650841);
        //API.songLyric(409650841);
        //getFlashPlayerUrl(1774490672);
        //getDetail(1774490672);
        //geAlbumDetail(1136455538);
    }
}
