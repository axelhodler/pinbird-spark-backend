package earth.xor.db;

import java.util.Date;

import com.google.gson.annotations.SerializedName;


public class Url {
    
    private String url;
    private String title;
    private String user;
    private Date timestamp;
    
    @SerializedName(value = "_id")
    private String objectId;
    
    public Url(String url, String title, String user, Date date) {
	this.setUrl(url);
	this.setTitle(title);
	this.setUser(user);
	this.timestamp = date;
    }
    
    public Url(String url, String title, String user) {
	this.setUrl(url);
	this.setTitle(title);
	this.setUser(user);
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public String getObjectId() {
	return this.objectId;
    }
    
    public void setObjectId(String objectId) {
	this.objectId = objectId;
    }

    public Date getTimeStamp() {
	return this.timestamp;
    }
}
