package com.ensa.videots;

import com.google.gson.annotations.SerializedName;

public class VideoItems {
    public PageInfo pageInfo;
    public String nextPageToken;
    public String prevPageToken;
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
            public String channelTitle;
            public String title;
            public String description;
            public Thumbnails thumbnails;

            public class Thumbnails {
                @SerializedName("medium")
                public Medium mediumImage;
                @SerializedName("default")
                public Default defaultImage;
                @SerializedName("high")
                public High high;

                public class Medium {
                    public String url;
                    public int width;
                    public int height;
                }

                public class Default {
                    public String url;
                    public int width;
                    public int height;
                }
                public class High {
                    public String url;
                    public int width;
                    public int height;
                }
            }
        }

    }
}
