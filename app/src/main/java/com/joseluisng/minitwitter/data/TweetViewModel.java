package com.joseluisng.minitwitter.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.joseluisng.minitwitter.retrofit.response.Tweet;
import com.joseluisng.minitwitter.ui.tweets.BottomModelTweetFragment;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;


    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets(){return tweets;}

    public void openDialogMenu(Context ctx, int idTweet){
        BottomModelTweetFragment dialogTweet = BottomModelTweetFragment.newInstance(idTweet);
        dialogTweet.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "BottomModelTweetFragment");

    }

    public LiveData<List<Tweet>> getFavTweets(){
        favTweets = tweetRepository.getFavTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewTweets(){
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets(){
        getNewTweets();
        return getFavTweets();
    }

    public void insertTweet(String mensaje){
        tweetRepository.createTweet(mensaje);
    }

    public void deleteTweet(int idTweet){
        tweetRepository.deleteTweet(idTweet);
    }

    public void likeTweet(int idTweet){
        tweetRepository.likeTweet(idTweet);
    }

}
