package earth.xor.model;

import com.google.gson.annotations.SerializedName;

public class Link {

    private String url;
    private String title;
    private String user;
    private String timestamp;

    @SerializedName(value = "_id")
    private String objectId;

    private Link(Builder builder) {
        this.objectId = builder.objectId;
        this.url = builder.url;
        this.title = builder.title;
        this.user = builder.user;
        this.timestamp = builder.timestamp;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String getTimeStamp() {
        return this.timestamp;
    }

    public static class Builder {
        private String url = null;
        private String title = null;
        private String user = null;
        private String timestamp = null;
        private String objectId = null;

        public Builder objectId(String objectId) {
            this.objectId = objectId;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Link build() {
            return new Link(this);
        }
    }
}
