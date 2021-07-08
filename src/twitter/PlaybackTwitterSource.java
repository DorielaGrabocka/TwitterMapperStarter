package twitter;

import twitter4j.Status;
import util.ObjectSource;

/**
 * A Twitter source that plays back a recorded stream of tweets.
 *
 * It ignores the set of terms provided except it uses the first call to setFilterTerms
 * as a signal to begin playback of the recorded stream of tweets.
 *
 * Implements Observable - each tweet is signalled to all observers
 */
public class PlaybackTwitterSource extends TwitterSource {
    // The speedup to apply to the recorded stream of tweets; 2 means play at twice the rate
    // at which the tweets were recorded
    private final double speedup;
    private ObjectSource source ;
    private boolean threadStarted;

    public PlaybackTwitterSource() {
        speedup = 1.0;
    }

    public PlaybackTwitterSource(double speedup) {
        this.speedup = speedup;
        source = new ObjectSource("data/TwitterCapture.jobj");
        threadStarted = false;
    }

    private void startThread() {
        if (threadStarted) return;
        threadStarted = true;
        Thread t = new PlaybackTwitterThread(this);
        t.start();
    }

    public double getSpeedup() {
        return speedup;
    }

    public ObjectSource getSource() {
        return source;
    }

    /**
     * The playback source merely starts the playback thread, it it hasn't been started already
     */
    protected void sync() {
        System.out.println("Starting playback thread with " + terms);

        startThread();
    }
}
