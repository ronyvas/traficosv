package com.rvasquez.traficosv.demo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rvasquez.traficosv.demo.R;
import com.squareup.picasso.Picasso;
import twitter4j.Status;

/**
 * Created by Vasquez on 06/07/13.
 */
public class TweetDetailFragment extends Fragment {

    private static String ARGS_STATUS = "status";

    private ImageView mImageViewUser;
    private ImageView mImageViewTweetDetail;
    private ImageView mImageViewBackground;
    private TextView mTextViewUserName;
    private TextView mTextViewUser;
    private TextView mTextViewTimeAgo;
    private TextView mTextViewContent;
    private TextView mTextViewTweetDescription;
    private TextView mTextViewFollowersCount;
    private TextView mTextViewFollowingCount;
    private TextView mTextViewTweetsCount;
    private LinearLayout mLinearLayoutTweetCard;

    private Status mStatus;

    public TweetDetailFragment() {
    }

    public static TweetDetailFragment getInstance(Status status) {
        TweetDetailFragment fragment = new TweetDetailFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARGS_STATUS, status);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mStatus = (Status) getArguments().getSerializable(ARGS_STATUS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.frag_tweet_detail, container, false);
        mImageViewTweetDetail = (ImageView) v.findViewById(R.id.imageViewTweetDetail);
        mImageViewUser = (ImageView) v.findViewById(R.id.imageViewTweetUserDetail);
        mImageViewBackground = (ImageView) v.findViewById(R.id.imageViewTweetBackground);
        mTextViewUserName = (TextView) v.findViewById(R.id.textViewTweetUserNameDetail);
        mTextViewUser = (TextView) v.findViewById(R.id.textViewTweetUserDetail);
        mTextViewContent = (TextView) v.findViewById(R.id.textViewTweetContentDetail);
        mTextViewTimeAgo = (TextView) v.findViewById(R.id.textViewTweetTimeDetail);
        mTextViewTweetDescription = (TextView) v.findViewById(R.id.textViewTweetDescription);
        mTextViewFollowersCount = (TextView) v.findViewById(R.id.textViewFollowersCount);
        mTextViewFollowingCount = (TextView) v.findViewById(R.id.textViewFollowingCount);
        mTextViewTweetsCount = (TextView) v.findViewById(R.id.textViewTweetsCount);
        mLinearLayoutTweetCard = (LinearLayout) v.findViewById(R.id.linearLayoutTweetCard);
        bindViews(mStatus);
        return v;
    }

    public void bindViews(Status status) {

        if (status != null) {
            mLinearLayoutTweetCard.setVisibility(View.VISIBLE);

            Picasso.with(getActivity()).load(status.getUser().getProfileImageURL()).into(mImageViewUser);
            Picasso.with(getActivity()).load(status.getUser().getProfileBackgroundImageURL()).into(mImageViewBackground);

            mTextViewUser.setText(String.format("@%s", status.getUser().getScreenName()));
            mTextViewUserName.setText(status.getUser().getName());
            mTextViewContent.setText(status.getText());
            mTextViewTweetDescription.setText(status.getUser().getDescription());
            mTextViewFollowingCount.setText(String.valueOf(status.getUser().getFriendsCount()));
            mTextViewFollowersCount.setText(String.valueOf(status.getUser().getFollowersCount()));
            mTextViewTweetsCount.setText(String.valueOf(status.getUser().getStatusesCount()));

            long createdAt = status.getCreatedAt().getTime();
            long now = System.currentTimeMillis();

            mTextViewTimeAgo.setText(DateUtils.getRelativeTimeSpanString(createdAt, now, DateUtils.FORMAT_ABBREV_ALL));

            if (status.getMediaEntities().length > 0) {
                String imageUrl = status.getMediaEntities()[0].getMediaURL();
                Picasso.with(getActivity()).load(imageUrl).into(mImageViewTweetDetail);
            }
            else {
                mImageViewTweetDetail.setImageBitmap(null);
            }
        }
        else {
            mLinearLayoutTweetCard.setVisibility(View.GONE);
        }

    }
}
