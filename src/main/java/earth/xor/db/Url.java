package earth.xor.db;

public class Url {
    
    private String url;
    private String title;
    private String user;
    
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

    public Object getObjectId() {
	// TODO Auto-generated method stub
	return null;
    }
}
