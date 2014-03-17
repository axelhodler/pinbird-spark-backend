package org.xorrr.util;

import earth.xor.model.Bookmark;

public class BookmarkObjects {

    public static final Bookmark testBookmark1 = new Bookmark.Builder()
            .url("http://www.foo.org").title("foo").user("user1").build();

    public static final Bookmark testBookmark2 = new Bookmark.Builder()
            .url("http://www.bar.org").title("bar").user("user2").build();

    public static final Bookmark testLink3 = new Bookmark.Builder()
            .url("http://www.baz.org").title("baz").user("user3").build();
}
