package com.rvasquez.traficosv.demo.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.rvasquez.traficosv.demo.R;
import com.squareup.picasso.Picasso;
import twitter4j.Status;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by Vasquez on 06/07/13.
 */
public class TweetListAdapter extends ArrayAdapter<Status> {

    private static int RES_ID=R.layout.adapter_tweet;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Status> mListStatus;

    public TweetListAdapter(Context context, List<Status> list) {
        super(context,RES_ID,list);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mListStatus = list;
    }

    @Override
    public int getCount() {
        return mListStatus.size();
    }

    @Override
    public Status getItem(int position) {
        return mListStatus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_tweet, viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.user=(TextView)view.findViewById(R.id.textViewTweetUserAdapter);
            viewHolder.username=(TextView)view.findViewById(R.id.textViewTweetUserNameAdapter);
            viewHolder.content=(TextView)view.findViewById(R.id.textViewTweetContentAdapter);
            viewHolder.time = (TextView)view.findViewById(R.id.textViewTweetTimeAdapter);
            viewHolder.image=(ImageView)view.findViewById(R.id.imageViewTweetAdapter);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        bindViews(viewHolder, position);
        return view;
    }

    private void bindViews(ViewHolder viewHolder, int position) {

        Status status=mListStatus.get(position);
        viewHolder.username.setText(status.getUser().getName());
        viewHolder.user.setText("@"+status.getUser().getScreenName());
        viewHolder.content.setText(status.getText());

        long before=status.getCreatedAt().getTime();
        long now=System.currentTimeMillis();

        viewHolder.time.setText(DateUtils.getRelativeTimeSpanString(before,now,DateUtils.FORMAT_ABBREV_ALL));
        Picasso.with(mContext).load(status.getUser().getProfileImageURL()).into(viewHolder.image);
    }

    class ViewHolder {
        ImageView image;
        TextView content;
        TextView time;
        TextView username;
        TextView user;

        public ViewHolder() {
        }
    }
}
