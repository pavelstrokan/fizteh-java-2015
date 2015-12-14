package ru.fizteh.fivt.students.StrokanPavel.TwitterStream;


import com.beust.jcommander.Parameter;

/**
 * Created by pavel on 12/2/15.
 */
public class Parameters {
    @Parameter(names = {"--query", "-q"}, description = "Search query")
    private String query = "";

    @Parameter (names = {"--stream", "-s"}, description = "Stream mode - prints tweets continuously")
    private Boolean streamMode = false;

    @Parameter (names = {"--hideRetweets"}, description = "Should retweets be hidden?")
    private Boolean hideRetweets = false;

    @Parameter (names = {"--limit", "-l"}, description = "Restriction on amount of tweets")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter (names = {"--help", "-h"}, description = "Help mode")
    private Boolean helpMode = false;

    @Parameter(names = {"--place", "-p" }, description = "Preferred places restriction")
    public String place = "nearby";

    public String getKeyword() {
        return query;
    }
    public Boolean isStreamMode() {
        return streamMode;
    }
    public Boolean isHideRetweets() {
        return hideRetweets;
    }
    public Integer getLimit() {
        return limit;
    }
    public Boolean isHelpMode() {
        return helpMode;
    }
    public String getPlace() {
        return place;
    }
}
