package org.zky.tool.magnetsearch.network;

/**
 *
 * Created by zhangkun on 2017/3/31.
 */

public class VideoDataEntity {

    /**
     * mode : xf
     * play_ftn : fc5354788a33b0a61697e624e1a8ceeb3728401678d606f50e405f285269aad846b177960c131db6cd7b5b463f7c9d9c5fcf1bb5bc202648069e2d56b4d8af13
     * code : 0a901ac9
     * play_host : xfcd.ctfs.ftn.qq.com
     * play_url : http://xfcd.ctfs.ftn.qq.com/ftn_handler/fc5354788a33b0a61697e624e1a8ceeb3728401678d606f50e405f285269aad846b177960c131db6cd7b5b463f7c9d9c5fcf1bb5bc202648069e2d56b4d8af13
     * play_url_cookie : FTN5K=0a901ac9
     *
     * "name": "[FHD]MIRD-136.wmv",
     " size": "10.05GB",
     " data": "363830643266343066616163656632646636653465343561303233346666343235366364646434332c31302c3133363538"
     */

    private String mode;
    private String play_ftn;
    private String code;
    private String play_host;
    private String play_url;
    private String play_url_cookie;
    private String name;
    private String size;
    private String data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPlay_ftn() {
        return play_ftn;
    }

    public void setPlay_ftn(String play_ftn) {
        this.play_ftn = play_ftn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlay_host() {
        return play_host;
    }

    public void setPlay_host(String play_host) {
        this.play_host = play_host;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getPlay_url_cookie() {
        return play_url_cookie;
    }

    public void setPlay_url_cookie(String play_url_cookie) {
        this.play_url_cookie = play_url_cookie;
    }

    @Override
    public String toString() {
        return "VideoDataEntity{" +
                "mode='" + mode + '\'' +
                ", play_ftn='" + play_ftn + '\'' +
                ", code='" + code + '\'' +
                ", play_host='" + play_host + '\'' +
                ", play_url='" + play_url + '\'' +
                ", play_url_cookie='" + play_url_cookie + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
