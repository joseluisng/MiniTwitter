package com.joseluisng.minitwitter.ui.tweets;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joseluisng.minitwitter.MyTweetRecyclerViewAdapter;
import com.joseluisng.minitwitter.R;
import com.joseluisng.minitwitter.common.Constantes;
import com.joseluisng.minitwitter.data.TweetViewModel;
import com.joseluisng.minitwitter.retrofit.response.Tweet;

import java.util.List;


public class TweetListFragment extends Fragment {


    private int tweetListType = 1;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    MyTweetRecyclerViewAdapter adapter;
    List<Tweet> tweetList;
    TweetViewModel tweetViewModel;


    public TweetListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = ViewModelProviders.of(getActivity())
                .get(TweetViewModel.class);

        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constantes.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        // Set the adapter

            Context context = view.getContext();
            recyclerView = view.findViewById(R.id.list);
            swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAzul));

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    if(tweetListType == Constantes.TWEET_LIST_ALL){
                        loadNewData();
                    }else if(tweetListType == Constantes.TWEET_LIST_FAVS){
                        loadFavNewData();
                    }

                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(context));


            adapter = new MyTweetRecyclerViewAdapter(getActivity(), tweetList);
            recyclerView.setAdapter(adapter);

            if(tweetListType == Constantes.TWEET_LIST_ALL){
                loadTweetData();
            }else if(tweetListType == Constantes.TWEET_LIST_FAVS){
                loadFavTweetData();
            }

        return view;
    }


    private void loadFavTweetData() {
        tweetViewModel.getNewFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewFavTweets().removeObserver(this);
            }
        });
    }

    private void loadFavNewData(){
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweetList);
            }
        });

    }


    private void loadTweetData() {
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweetList);

            }
        });
    }

    private void loadNewData() {
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });
    }

}
