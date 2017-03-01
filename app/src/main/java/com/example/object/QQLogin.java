package com.example.object;

/**
 * Created by wangqiang on 2016/7/29.
 */
public class QQLogin {
    private String openID = "";
    private String nickName = "";
    private String city = "";
    private String imageUrl = "";
    public String getImageUrl(){
        return imageUrl;
    }
    public void setImageUri(String ImageUrl){
        this.imageUrl = imageUrl;
    }

    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getNickName(){
        return nickName;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    public String getOpenID(){
        return openID;
    }
    public void setOpenID(String openID){
        this.openID = openID;
    }
}
