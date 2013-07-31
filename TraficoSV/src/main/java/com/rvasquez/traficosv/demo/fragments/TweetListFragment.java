package com.rvasquez.traficosv.demo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.rvasquez.traficosv.demo.R;
import com.rvasquez.traficosv.demo.TraficoSV;
import com.rvasquez.traficosv.demo.adapters.TweetListAdapter;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;


/**
 * Created by Vasquez on 06/07/13.
 */
public class TweetListFragment extends SherlockListFragment {
    private TweetListAdapter mAdapter;
    private OnTweetSelectedListener mCallback;
    private ProgressBar mProgressBar;

    public static TweetListFragment getInstance() {
        return new TweetListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tweet_list, container, false);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBarTweetList);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null) {
            new SearForTweets().execute();
        } else {
            mProgressBar.setVisibility(View.GONE);
            getListView().setAdapter(mAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.tweetIsSelected(mAdapter.getItem(position));
    }

    //Esto asegura que la actividad padre ha implementado
    //la interfaz del callback. Si no, mostrara una Excepcion.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnTweetSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnTtwetSelectedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                mProgressBar.setVisibility(View.VISIBLE);
                new SearForTweets().execute();
                return true;
            case R.id.action_compose:
                try {
                    String url = "https://twitter.com/intent/tweet?source=webclient&text=#TraficoSV";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "App Twitter no instalada", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface OnTweetSelectedListener {
        public void tweetIsSelected(Status s);
    }

    private class SearForTweets extends AsyncTask<Void, Integer, List<twitter4j.Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(Void... voids) {
            ConfigurationBuilder config = new ConfigurationBuilder();
            config.setDebugEnabled(true)
                    .setOAuthConsumerKey(TraficoSV.TWITTER_API_KEY)
                    .setOAuthConsumerSecret(TraficoSV.TWITTER_API_SECRET)
                    .setOAuthAccessToken(TraficoSV.TWITTER_ACCESS_TOKEN)
                    .setOAuthAccessTokenSecret(TraficoSV.TWITTER_ACCESS_TOKEN_SECRET);
            TwitterFactory factory = new TwitterFactory(config.build());
            Twitter twitter = factory.getInstance();
            Query query = new Query("TraficoSV");
            List<twitter4j.Status> statusList = null;
            try {
                QueryResult result = twitter.search(query);
                statusList = result.getTweets();
                }
            catch (TwitterException e) {
                e.printStackTrace();
                }
            return statusList;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            mProgressBar.setVisibility(View.GONE);
            mAdapter = new TweetListAdapter(getActivity(), statuses);
            getListView().setAdapter(mAdapter);

        }
    }
}
