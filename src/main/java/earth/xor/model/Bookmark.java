package earth.xor.model;

import com.google.gson.annotations.SerializedName;

public class Bookmark {

    private String url;
    private String title;
    private String user;
    private String timestamp;

    @SerializedName(value = "_id")
    private String objectId;

    private Bookmark(Builder builder) {
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

        public Bookmark build() {
            return new Bookmark(this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((objectId == null) ? 0 : objectId.hashCode());
        result = prime * result
                + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bookmark other = (Bookmark) obj;
        if (objectId == null) {
            if (other.objectId != null)
                return false;
        } else if (!objectId.equals(other.objectId))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Bookmark [url=" + url + ", title=" + title + ", user=" + user
                + ", timestamp=" + timestamp + ", objectId=" + objectId + "]";
    }
}
