package twitter;

public class TwitterSourceFactory {
    public static TwitterSource createLiveTwitterSource(){
        return new LiveTwitterSource();
    }

    public static TwitterSource createPlaybackTwitterSource(double speedUp){
        return new PlaybackTwitterSource(speedUp);
    }


}
