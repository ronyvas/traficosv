package com.rvasquez.traficosv.demo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.rvasquez.traficosv.demo.R;
import com.rvasquez.traficosv.demo.fragments.TweetDetailFragment;
import com.rvasquez.traficosv.demo.fragments.TweetListFragment;
import twitter4j.Status;

public class MainActivity extends SherlockFragmentActivity implements TweetListFragment.OnTweetSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null &&
                getSupportFragmentManager().findFragmentById(R.id.fragTweetListFragment)==null) {

            TweetListFragment fragment = TweetListFragment.getInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();

        }
    }

    @Override
    public void tweetIsSelected(Status status) {

        TweetDetailFragment fragment=(TweetDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragTweetDetailFragment);

        if(fragment!=null && fragment.isInLayout()){
            fragment.bindViews(status);
        }
        else{
            fragment=TweetDetailFragment.getInstance(status);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
