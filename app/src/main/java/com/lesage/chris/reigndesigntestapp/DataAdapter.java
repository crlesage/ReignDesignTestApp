package com.lesage.chris.reigndesigntestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Chris on 1/15/2016.
 */
public class DataAdapter extends BaseAdapter {

    private ArrayList<Hits> mData = new ArrayList<Hits>();
    private LayoutInflater mInflater;

    public DataAdapter(Context mContext) {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(Hits hit) {
        hit.position = mData.size();
        mData.add(hit);
    }

    public void removeAllItems(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Hits getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view;
        if (convertView == null) {
            view = (LinearLayout) mInflater.inflate(R.layout.single_row_display, parent, false);
        } else {
            view = (LinearLayout) convertView;
        }
        try {
            view = makeView(view, position, parent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    protected LinearLayout makeView(LinearLayout theView, int position, ViewGroup parent) throws ParseException {
        String title = getItem(position).getTitle();
        String storyTitle = getItem(position).getStory_title();
        String author = getItem(position).getAuthor().trim();
        String created_at_i = String.valueOf(getItem(position).getCreated_at_i());

        Long time = Long.valueOf(created_at_i);
        String actual_created_at = getTimeAgo(time);

        TextView titleText = (TextView) theView.findViewById(R.id.title_text);
        TextView authorText = (TextView) theView.findViewById(R.id.author_text);

        if(getItem(position).getTitle() == null || getItem(position).getTitle().equals("")){
            titleText.setText(storyTitle);
        } else {
            titleText.setText(title);
        }
        authorText.setText(author + " - " + actual_created_at);

        return theView;
    }

    /*
     * Copyright 2012 Google Inc.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "time";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1h";
        }else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "h";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + "d";
        }
    }


}
