package com.ensa.videots;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class VideoItems {
    public PageInfo pageInfo;
    public Item[] items;

    public class PageInfo {
        public int totalResults;
        public int resultsPerPage;
    }

    public class Item {
        public Id id;
        public Snippet snippet;

        public class Id {
            public String videoId;
        }

        public class Snippet {
            public String publishedAt;
            public String channelId;
            public String title;
            public String description;
            public Thumbnails thumbnails;

            public class Thumbnails {
                public Medium medium;
                @SerializedName("default")
                public Default defaultImage;

                public class Medium {
                    public String url;
                    public int width;
                    public int height;
                }
                public class Default{
                    public String url;
                    public int width;
                    public int height;
                }
            }
        }

    }
}
