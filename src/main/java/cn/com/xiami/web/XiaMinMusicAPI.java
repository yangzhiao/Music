package cn.com.xiami.web;

import cn.com.xiami.entity.Song;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 基于虾米接口的调用
 * @Author YZA(yangzhiao@163.com)
 * @Date 2017/3/25 18:06.
 */
@Controller
@RequestMapping("/xiaMi")
public class XiaMinMusicAPI {
    private Logger logger = LoggerFactory.getLogger(XiaMinMusicAPI.class);
    //模拟请求的代理头
    private static final String UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.75 Safari/537.36";
    private static String COOKIE="user_from=2;XMPLAYER_addSongsToggler=0;XMPLAYER_isOpen=0;_xiamitoken=cb8bfadfe130abdbf5e2282c30f0b39a;";

    /**
     * @Description 模拟请求公共方法
     * @param map
     * @param url
     * @param referrerUrl
     * @return
     * @throws IOException
     */
    public static String cUrl(Map param, String url, String referrerUrl) throws Exception {
        param.put("v", "2.0");
        param.put("app_key", "1");
        param.put("page", "1");
        param.put("limit", "5");
        String content = Jsoup.connect(url)
                .data(param)
                .userAgent(UA)
                .cookie("auth", "token")
                .referrer("http://h.xiami.com/")
                .timeout(6000)
                .ignoreContentType(true)
                .get().html();
        return content;
    }
    /**
     * 根据关键词搜索歌曲
     *
     * @param key
     *            关键词
     * @return
     * @throws IOException
     */
    @RequestMapping("/search/songs/{key}")
    @ResponseBody
    public Object seachXiaMiSongs(@PathVariable String key){
        Map resultMap = new HashMap();
        try {
            String url = "http://api.xiami.com/web";
            Map param = new HashMap();
            param.put("r", "search/songs");
            param.put("key", key);
            String content = cUrl(param,url,"http://h.xiami.com/");
            logger.debug("search/songs--->总结构====" + content);
            Document doc = Jsoup.parse(content);
            Element element = doc.body();
            String body = element.html();
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("search/songs--->序列化后的内容===" + jsonObject.toJSONString());
            JSONObject data = jsonObject.getJSONObject("data");
            Object message = jsonObject.get("message");
            Object request_id = jsonObject.get("request_id");
            Integer state = jsonObject.getInteger("state");
            if (state == null || state != 0) {
                logger.error("search/songs--->state的类容===" + state);
                throw new ServerException(message.toString());
            }
            logger.debug("search/songs--->data的类容===" + data);
            Object next = data.get("next");
            Object previous = data.get("previous");
            List songs = data.getJSONArray("songs");
            Object total = data.get("total");
            logger.debug("search/songs--->songs的类容===" + songs);
            resultMap.put("resule", 1);
            resultMap.put("state", state);
            resultMap.put("message", message);
            //resultMap.put("total", total);
            //resultMap.put("songs", songs);
            resultMap.put("data", data);
        } catch (Exception e){
            logger.error("search/songs--->" + e.getMessage());
            resultMap.put("resule", 0);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    /**
     * 获取艺术家歌曲列表
     * @param artistId
     *          艺术家id
     * @return
     * @throws IOException
     */
    @RequestMapping("/artist/hot-songs/{artistId}")
    @ResponseBody
    public Object geTartList(@PathVariable Integer artistId) {
        Map resultMap = new HashMap();
        try {
            String url = "http://api.xiami.com/web";
            Map param = new HashMap();
            param.put("r", "artist/hot-songs");
            param.put("id", artistId.toString());
            String content = cUrl(param,url,"http://h.xiami.com/");
            logger.debug("artist/hot-songs--->总结构====" + content);
            Document doc = Jsoup.parse(content);
            Element element = doc.body();
            String body = element.html();
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("artist/hot-songs--->序列化后的内容===" + jsonObject);
            Integer state = jsonObject.getInteger("state");
            Object message = jsonObject.get("message");
            if (state == null || state !=0) {
                logger.error("artist/hot-songs--->state的类容===" + state);
                throw new ServerException(message.toString());
            }
            List data = jsonObject.getJSONArray("data");
            Object request_id = jsonObject.get("request_id");
            logger.debug("artist/hot-songs--->data的类容===" + data);
            resultMap.put("resule", 0);
            resultMap.put("state", state);
            resultMap.put("message", message);
            resultMap.put("data", data);
        } catch (Exception e){
            logger.error("artist/hot-songs--->" + e.getMessage());
            resultMap.put("resule", 0);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    /**
     * 获取专辑详情
     * @param AlbumId
     *          专辑id
     * @return
     * @throws IOException
     */
    @RequestMapping("/album/detail/{albumId}")
    @ResponseBody
    public Object geAlbumDetail(@PathVariable Integer albumId) {
        Map resultMap = new HashMap();
        try {
            String url = "http://api.xiami.com/web";
            Map param = new HashMap();
            param.put("r", "album/detail");
            param.put("id", albumId.toString());
            String content = cUrl(param,url,"http://h.xiami.com/");
            logger.debug("album/detail--->总结构====" + content);
            Document doc = Jsoup.parse(content);
            Element element = doc.body();
            String body = element.html();
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("album/detail--->序列化后的内容===" + jsonObject);
            Integer state = jsonObject.getInteger("state");
            Object message = jsonObject.get("message");
            if (state == null || state !=0) {
                logger.error("album/detail--->state的类容===" + state);
                throw new ServerException(message.toString());
            }
            Object request_id = jsonObject.get("request_id");
            JSONObject data = jsonObject.getJSONObject("data");
            logger.debug("album/detail--->data的类容===" + data);
            resultMap.put("resule", 0);
            resultMap.put("state", state);
            resultMap.put("message", message);
            resultMap.put("data", data);
        } catch (Exception e){
            logger.error("album/detail--->" + e.getMessage());
            resultMap.put("resule", 0);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    /**
     * 获取作者详情
     * @param songId  eg:/song/1769908356
     * @return
     */
    @RequestMapping("/playlist/{songId}")
    @ResponseBody
    public Object getFlashPlayerUrl(@PathVariable Integer songId) {
        Map resultMap = new HashMap();
        try {
            String url = "http://www.xiami.com/song/playlist/id/"+ songId +"/object_id/0/cat/json";
            Map param = new HashMap();
            String content = cUrl(param,url,"http://www.xiami.com/song/playlist/id/" + songId);
            logger.debug("playlist--->总结构====" + content);
            Document doc = Jsoup.parse(content);
            Element element = doc.body();
            String body = element.html();
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("playlist--->序列化后的内容===" + jsonObject);
            Boolean status = jsonObject.getBoolean("status");
            Object message = jsonObject.get("message");
            if (status == null || !status) {
                logger.error("playlist--->status的类容===" + status);
                throw new ServerException(message.toString());
            }
            JSONObject data = jsonObject.getJSONObject("data");
            logger.debug("playlist--->data的类容===" + data);
            List trackList = data.getJSONArray("trackList");
            Map track = (Map) trackList.get(0);
            Object albumId=track.get("albumId");
            resultMap.put("resule", 0);
            resultMap.put("message", message);
            resultMap.put("data", data);
        } catch (Exception e){
            logger.error("playlist--->" + e.getMessage());
            resultMap.put("resule", 0);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取歌曲详情
     * @param songId  eg:1769908356
     * @return
     */
    @RequestMapping("/song/detail/{songId}")
    @ResponseBody
    public Object getDetail(@PathVariable Integer songId) {
        Map resultMap = new HashMap();
        try {
            String url = "http://api.xiami.com/web";
            Map param = new HashMap();
            param.put("r", "song/detail");
            param.put("id", songId.toString());
            String content = cUrl(param,url,"http://h.xiami.com/");
            logger.debug("song/detail--->总结构====" + content);
            Document doc = Jsoup.parse(content);
            Element element = doc.body();
            String body = element.html();
            String format = StringEscapeUtils.unescapeHtml(body);
            JSONObject jsonObject = JSONObject.parseObject(format);
            logger.debug("song/detail--->序列化后的内容===" + jsonObject);
            Integer state = jsonObject.getInteger("state");
            Object message = jsonObject.get("message");
            if (state == null || state !=0) {
                logger.error("song/detail--->state的类容===" + state);
                throw new ServerException(message.toString());
            }
            JSONObject data = jsonObject.getJSONObject("data");
            logger.debug("song/detail--->data的类容===" + data);
            Song song = (Song) data.getObject("song",Song.class);
            logger.debug("song/detail--->song的类容===" + song);
            resultMap.put("resule", 0);
            resultMap.put("state", state);
            resultMap.put("message", message);
            resultMap.put("data", data);
        } catch (Exception e){
            logger.error("song/detail--->" + e.getMessage());
            resultMap.put("resule", 0);
            resultMap.put("message", e.getMessage());
        }
            return resultMap;
    }

    /**
     * 获取歌曲的封面图片地址
     * @param song
     * @return
     * @throws IOException
     */
    /*public static Song getXiamiSongDetail(Song song) throws IOException {
        Document doc = Jsoup
                .connect("http://www.xiami.com" + song.getSongUrl())

                .data("query", "Java").userAgent("Mozilla")

                .cookie("auth", "token")

                .timeout(6000)

                .get();
        Element e = doc.getElementById("albumCover");
        Element img = e.select("img").get(0);
        String imagSrc = img.attr("src");
        System.out.print(imagSrc);
        song.setImage(imagSrc);

        return song;
    }*/

    /**
     * 解析下载地址
     * @param location
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String xiamidecode(String location) throws UnsupportedEncodingException {
        int _local10;
        int _local2 = Integer.parseInt(location.substring(0, 1));
        String _local3 = location.substring(1, location.length());
        double _local4 = Math.floor(_local3.length() / _local2);
        int _local5 = _local3.length() % _local2;
        String[] _local6 = new String[_local2];
        int _local7 = 0;
        while (_local7 < _local5) {
            if (_local6[_local7] == null) {
                _local6[_local7] = "";
            }
            _local6[_local7] = _local3.substring((((int) _local4 + 1) * _local7),
                    (((int) _local4 + 1) * _local7) + ((int) _local4 + 1));
            _local7++;
        }
        _local7 = _local5;
        while (_local7 < _local2) {
            _local6[_local7] = _local3
                    .substring((((int) _local4 * (_local7 - _local5)) + (((int) _local4 + 1) * _local5)),
                            (((int) _local4 * (_local7 - _local5)) + (((int) _local4 + 1) * _local5))+(int) _local4);
            _local7++;
        }
        String _local8 = "";
        _local7 = 0;
        while (_local7 < ((String) _local6[0]).length()) {
            _local10 = 0;
            while (_local10 < _local6.length) {
                if (_local7 >= _local6[_local10].length()) {
                    break;
                }
                _local8 = (_local8 + _local6[_local10].charAt(_local7));
                _local10++;
            }
            _local7++;
        }
        _local8 = URLDecoder.decode(_local8, "utf8");
        String _local9 = "";
        _local7 = 0;
        while (_local7 < _local8.length()) {
            if (_local8.charAt(_local7) == '^'){
                _local9 = (_local9 + "0");
            } else {
                _local9 = (_local9 + _local8.charAt(_local7));
            };
            _local7++;
        }
        _local9 = _local9.replace("+", " ");
        return _local9;
    }

    /*public static void main(String args[]) throws IOException {
        XiaMinMusicAPI api=new XiaMinMusicAPI();
        api.seachXiaMiSongs(null);
        api.geTartList(7158);
        api.getFlashPlayerUrl(1774490672);
        api.getDetail(1774490672);
        api.geAlbumDetail(1136455538);
    }*/
}
