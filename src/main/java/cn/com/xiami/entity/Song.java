package cn.com.xiami.entity;

/**
 * Created by ASUS on 2017/3/22.
 */
public class Song {
    /**
     * album_id : 1136455538
     * album_name : 我的少女时代 电影歌曲原声带
     * artist_id : 7158
     * artist_name : 原声带
     * listen_file : http://om5.alicdn.com/158/7158/1136455538/1774490672_1478671781016.mp3?auth_key=370e9fae2e040caae8a0327164f3af1a-1490842800-0-null
     * logo : http://pic.xiami.net/images/album/img58/7158/11364555381469591559_1.jpg
     * lyric : http://img.xiami.net/lyric/72/1774490672_1450018195_824.trc
     * music_type : 0
     * need_pay_flag : 0
     * purview_roles : [{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"e"},{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"f"},{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"l"},{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"h"},{"operation_list":[{"purpose":1,"upgrade_role":3},{"purpose":2,"upgrade_role":3}],"quality":"s"}]
     * singers : 田馥甄
     * song_id : 1774490672
     * song_name : 小幸运
     */

    private int album_id;
    private String album_name;
    private int artist_id;
    private String artist_name;
    private String listen_file;
    private String logo;
    private String lyric;
    private int music_type;
    private int need_pay_flag;
    private String singers;
    private int song_id;
    private String song_name;

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getListen_file() {
        return listen_file;
    }

    public void setListen_file(String listen_file) {
        this.listen_file = listen_file;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public int getMusic_type() {
        return music_type;
    }

    public void setMusic_type(int music_type) {
        this.music_type = music_type;
    }

    public int getNeed_pay_flag() {
        return need_pay_flag;
    }

    public void setNeed_pay_flag(int need_pay_flag) {
        this.need_pay_flag = need_pay_flag;
    }

    public String getSingers() {
        return singers;
    }

    public void setSingers(String singers) {
        this.singers = singers;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }
}
