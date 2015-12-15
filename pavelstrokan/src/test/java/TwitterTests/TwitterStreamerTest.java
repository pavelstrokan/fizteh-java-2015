package TwitterTests;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;

import java.io.IOException;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import ru.fizteh.fivt.students.StrokanPavel.TwitterStream.GeoCoder;
import ru.fizteh.fivt.students.StrokanPavel.TwitterStream.Parameters;
import ru.fizteh.fivt.students.StrokanPavel.TwitterStream.TextFormatter;
import twitter4j.*;
import twitter4j.Twitter4jTestUtils;


import java.util.LinkedList;
import java.util.List;
/**
 * Created by pavel on 06.12.15.
 */
@RunWith(MockitoJUnitRunner.class)
//@PrepareForTest({TwitterStream.class, TwitterFactory.class, GeoCoder.class, /*TwitterPrinter.class*/})
public class TwitterStreamerTest extends TestCase {

    private List<Status> tweets;

//    @BeforeClass
//    public void setUp() throws JSONException, IOException, TwitterException {
//        Twitter twitter = PowerMockito.mock(Twitter.class);
//        tweets = Twitter4jTestUtils.tweetsFromJson("/tweets.json");
//        Parameters parameters = mock(Parameters.class);
//
//        PowerMockito.mockStatic(TextFormatter.class);
//        PowerMockito.mockStatic(TwitterFactory.class);
//        PowerMockito.when(TwitterFactory.getSingleton()).thenReturn(twitter);
//
//        QueryResult resultForJava = mock(QueryResult.class);
//        when(resultForJava.getTweets()).thenReturn(tweets);
//        when(twitter.search(argThat(hasProperty("query", equalTo("java"))))).thenReturn(resultForJava);
//
//        QueryResult emptyResult = mock(QueryResult.class);
//        when(emptyResult.getTweets()).thenReturn(new LinkedList<>());
//        when(twitter.search(argThat(hasProperty("query", not(equalTo("java")))))).thenReturn(emptyResult);
    }
}
