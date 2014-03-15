package org.xorrr.util;

import earth.xor.model.Link;

public class LinkObjects {

    public static final Link testLink1 = new Link.Builder()
            .url("http://www.foo.org").title("foo").user("user1").build();

    public static final Link testLink2 = new Link.Builder()
            .url("http://www.bar.org").title("bar").user("user2").build();

    public static final Link testLink3 = new Link.Builder()
            .url("http://www.baz.org").title("baz").user("user3").build();
}
