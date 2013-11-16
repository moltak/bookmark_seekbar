bookmark_seekbar
================

bookmark seekbar for android


```xml
<com.example.bookmark_seekbar.BookmarkSeekbar
        android:id="@+id/timeline"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"
        custom:bookmark_color="#88ff053a"
        custom:bookmark_width="3" />
```

```java
// add bookmarks for millisecond
seekbar.addBookmark(50 * 1000);
seekbar.addBookmark(120 * 1000);
seekbar.addBookmark(170 * 1000);
```

![](https://raw.github.com/moltak/bookmark_seekbar/master/bookmark_seekbar.png)
