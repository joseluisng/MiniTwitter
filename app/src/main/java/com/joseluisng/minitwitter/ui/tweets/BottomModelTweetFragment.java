package com.joseluisng.minitwitter.ui.tweets;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joseluisng.minitwitter.R;
import com.joseluisng.minitwitter.common.Constantes;
import com.joseluisng.minitwitter.data.TweetViewModel;

public class BottomModelTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel tweetViewModel;
    private int idTweetEliminar;

    public static BottomModelTweetFragment newInstance(int idTweet) {
        BottomModelTweetFragment fragment = new BottomModelTweetFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.ARG_TWEET_ID, idTweet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            idTweetEliminar = getArguments().getInt(Constantes.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_model_tweet_fragment, container, false);

        final NavigationView nav = v.findViewById(R.id.navigation_view_bottom_tweet);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.action_delete_tweet){
                    tweetViewModel.deleteTweet(idTweetEliminar);
                    getDialog().dismiss();
                    return true;
                }

                return false;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
        // TODO: Use the ViewModel
    }

}
