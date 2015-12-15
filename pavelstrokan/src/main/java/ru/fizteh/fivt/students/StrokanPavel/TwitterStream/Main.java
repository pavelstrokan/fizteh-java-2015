package ru.fizteh.fivt.students.StrokanPavel.TwitterStream;

import com.beust.jcommander.JCommander;
//import com.google.code.geocoder.*;//Geocoder;
//import com.google.code.geocoder.GeocoderRequestBuilder;
//import com.google.code.geocoder.model.GeocodeResponse;
//import com.google.code.geocoder.model.GeocoderRequest;
//import com.google.code.geocoder.model.GeocoderResult;
import twitter4j.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by pavel on 12/1/15.
 */
public class Main {
    private static final int SECOND = 1000;
    private static final int MAX_AMOUNT_OF_TRIES = 2;
    private static final int FOUR = 4;
    private static final int FORTY = 40;
    private static final String NEARBY = "nearby";
    public static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException, JSONException {
        Parameters parameters = new Parameters();
        JCommander jCommander = new JCommander(parameters, args);
        if (parameters.isHelpMode()) {
            jCommander.setProgramName("TwitterStream");
            jCommander.usage();
        } else {
            if (parameters.getKeyword().isEmpty()) {
                System.out.println("Empty query error\n"
                        + "Use TwitterStream --help for details");
            } else {
                try {
                    if (parameters.isStreamMode()) {
                        streamMode(parameters);
                    } else {
                        defaultMode(parameters);
                    }
                } catch (TwitterException ex) {
                    System.err.println("Connection error:\n"
                            + ex.getMessage());
                    System.exit(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String formatNickname(String nickname) {
        return ("@" + ANSI_BLUE + nickname + ANSI_RESET + ": ");
    }
    private static void printTweet(Status status, Parameters parameters) {
        StringBuilder completeTweet = new StringBuilder();
        if (!parameters.isStreamMode()) {
            long now = System.currentTimeMillis();
            completeTweet.append("[")
                    .append(TimeFormatter.formatDifferenceBetween(
                            status.getCreatedAt().getTime(), now))
                    .append("] ");
        }
        completeTweet.append(formatNickname(status.getUser().getScreenName()));
        if (status.isRetweet()) {
            if (parameters.isHideRetweets()) {
                return;
            }
            completeTweet.append("ретвитнул ");
            String[] retweetPart = status.getText().split(":");
            completeTweet.append(formatNickname(retweetPart[0].substring(FOUR)));
            for (int i = 1; i < retweetPart.length; ++i) {
                completeTweet.append(retweetPart[i]);
            }
        } else {
            completeTweet.append(status.getText());
            if (status.isRetweeted()) {
                completeTweet.append(" (")
                        .append(status.getRetweetCount())
                            .append(TextFormatter.retweetCorrectForm(
                                    status.getRetweetCount()))
                                        .append(") ");
            }
        }
        System.out.println(completeTweet.toString());
    }

    static Queue<Status> prepare(Parameters parameters) {
        Queue<Status> tweets = new LinkedList<>();
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getSingleton();
        twitter4j.StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (!parameters.isHideRetweets() || !tweet.isRetweet()) {
                    tweets.add(tweet);
                }
            }
            @Override
            public void onException(Exception e) {
                System.out.println("Listening error\n");
                System.out.println(e.getMessage());
                System.exit(1);
            }
        };
        twitterStream.addListener(listener);
        twitterStream.filter(new FilterQuery().track(parameters.getKeyword()));
        return tweets;
    }
    static void defaultMode(Parameters parameters) throws TwitterException, IOException,
            JSONException, InterruptedException {
        Twitter twitter;
        Query query;
        twitter = TwitterFactory.getSingleton();
        List<Status> tweets;
        GeoLocation myLocation = GeoCoder.getCoordinates(parameters.getPlace());
        int amountOfPrintedTweets = 0;
        int amountOfTries = 0;
        while (amountOfTries < MAX_AMOUNT_OF_TRIES) {
            try {
                query = new Query(parameters.getKeyword());
                query.setCount(parameters.getLimit());
                query.setGeoCode(myLocation, 50, Query.Unit.km);
                tweets = twitter.search(query).getTweets();
                for (Status tweet : tweets) {
                    printTweet(tweet, parameters);
                    amountOfPrintedTweets++;
                    if (amountOfPrintedTweets >= parameters.getLimit()) {
                        break;
                    }
                }
                amountOfTries = MAX_AMOUNT_OF_TRIES;
            } catch (TwitterException ex) {
                amountOfTries++;
                if (amountOfTries == MAX_AMOUNT_OF_TRIES) {
                    throw ex;
                }
            }
        }
    }

    static void streamMode(Parameters parameters) {
        Queue<Status> tweets = prepare(parameters);
        while (true) {
            if (!tweets.isEmpty()) {
                printTweet(tweets.poll(), parameters);
            }
            try {
                Thread.sleep(SECOND);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
