package twitter;

import twitter4j.Status;
import util.ObjectSource;

public class PlaybackTwitterThread extends Thread{
    private final long PLAYBACK_START_TIME;
    private long recordStartTime;
    private PlaybackTwitterSource playbackTwitterSource;

    public PlaybackTwitterThread(PlaybackTwitterSource p) {
        PLAYBACK_START_TIME = 1000 + System.currentTimeMillis();
        playbackTwitterSource = p;
        recordStartTime = 0;
    }

    public void run() {
        long now;
        ObjectSource source = playbackTwitterSource.getSource();
        while (true) {
            Object timeObject = source.readObject();
            if (timeObject == null)
                break;

            Object statusObject = source.readObject();
            if (statusObject == null)
                break;

            long statusTime = (Long)timeObject;

            if (recordStartTime == 0)
                recordStartTime = statusTime;

            Status status = (Status) statusObject;

            long playbackTime = computePlaybackTime(statusTime);

            while ((now = System.currentTimeMillis()) < playbackTime) {
                pause(playbackTime - now);
            }

            if (status.getPlace() != null) {
                playbackTwitterSource.handleTweet(status);
            }
        }
    }

    private long computePlaybackTime(long statusTime) {
        long statusDelta = statusTime - recordStartTime;
        long targetDelta = Math.round(statusDelta / playbackTwitterSource.getSpeedup());
        return PLAYBACK_START_TIME + targetDelta;
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
