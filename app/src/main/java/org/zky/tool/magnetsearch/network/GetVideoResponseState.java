package org.zky.tool.magnetsearch.network;

/**
 * Created by zhangkun on 2017/3/31.
 */

class GetVideoResponseState<T> {

    /**
     * code : 1
     * msg : 磁力信息获取成功
     * btvda : key申请联系QQ：1691523806 或 加讨论组：463413778  请注明：btvda key申请
     * data : [{"name":"[FHD]MIRD-136.wmv","size":"10.05GB","data":"363830643266343066616163656632646636653465343561303233346666343235366364646434332c31302c3133363538"}]
     */

    private int code;
    private String msg;
    private String btvda;
    /**
     * name : [FHD]MIRD-136.wmv
     * size : 10.05GB
     * data : 363830643266343066616163656632646636653465343561303233346666343235366364646434332c31302c3133363538
     */

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBtvda() {
        return btvda;
    }

    public void setBtvda(String btvda) {
        this.btvda = btvda;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
