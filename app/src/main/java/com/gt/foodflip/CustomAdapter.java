package com.gt.foodflip;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * CustomAdapter class for the purpose of giving all food entries in the search screen
 * custom layouts.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {
    public Context context;
    public Resources resources;

    private Activity activity;
    private ArrayList entries;
    private static LayoutInflater inflater = null;
    private FoodEntry foodEntry;
    ViewHolder holder;

    /**
     * CustomAdapter Constructor.
     *
     * @param activity the current Activity.
     * @param entries the arrayList being passed in (contains all food entries).
     */
    public CustomAdapter(Activity activity, ArrayList entries, Resources resources, Context context) {
        this.activity = activity;
        this.entries = entries;
        this.resources = resources;
        this.context = context;

        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /* Calculates the size of the passed in arrayList */
    public int getCount() {
        return entries.size() <= 0 ? 1 : entries.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView building;
        public TextView location;
        public TextView foodType;
        public TextView price;
        public TextView votes;
        public ImageButton downvote;
        public ImageButton upvote;
        public int id;
    }

    public View getView(int index, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.custom_entry_layout, null);

            holder = new ViewHolder();
            holder.building = (TextView) view.findViewById(R.id.building);
            holder.location = (TextView) view.findViewById(R.id.location);
            holder.foodType = (TextView) view.findViewById(R.id.food_type);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.votes = (TextView) view.findViewById(R.id.votes);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (entries.size() <= 0) {
            holder.building.setText("No Data");
        } else {
            foodEntry = (FoodEntry) entries.get(index);

            holder.building.setText(foodEntry.getBuilding());
            holder.location.setText(foodEntry.getLocation());
            holder.foodType.setText(foodEntry.getType());
            holder.price.setText(foodEntry.getPrice());
            holder.votes.setText(Integer.toString(foodEntry.getVotes()));
            holder.downvote = (ImageButton) view.findViewById(R.id.downvote);
            holder.upvote = (ImageButton) view.findViewById(R.id.upvote);
            holder.id = ((FoodEntry) entries.get(index)).getId();

            view.setOnClickListener(new OnItemClickListener(index));
            holder.downvote.setOnClickListener(new OnDownvoteClickListener(holder.votes, holder.id));
            holder.upvote.setOnClickListener(new OnUpvoteClickListener(holder.votes, holder.id));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "Row button clicked");
    }

    /**
     * This handles upvotes and setting the vote counter
     */
    private class OnUpvoteClickListener implements View.OnClickListener {
        private TextView votes;
        private int id;
        private SearchScreenActivity searchScreenActivity = (SearchScreenActivity) activity;

        OnUpvoteClickListener(TextView votes, int index) {
            this.votes = votes;
            this.id = index;
        }

        @Override
        public void onClick(View view) {
            System.out.println(this.id);
            if (searchScreenActivity.hasVoted(this.id))
                return;
            searchScreenActivity.setVote(this.id, 1);
            int currentVotes = Integer.parseInt(this.votes.getText().toString());
            this.votes.setText(Integer.toString(++currentVotes));
        }
    }

    /**
     * This handles downvotes and setting the vote counter
     */
    private class OnDownvoteClickListener implements View.OnClickListener {
        private TextView votes;
        private int id;
        private SearchScreenActivity searchScreenActivity = (SearchScreenActivity) activity;

        OnDownvoteClickListener(TextView votes, int id) {
            this.votes = votes;
            this.id = id;
        }

        @Override
        public void onClick(View view) {
            System.out.println(this.id);
            if (searchScreenActivity.hasVoted(this.id))
                return;
            searchScreenActivity.setVote(this.id, -1);
            int currentVotes = Integer.parseInt(this.votes.getText().toString());
            this.votes.setText(Integer.toString(--currentVotes));
        }
    }

    /**
     * Called when entry clicked in ListView. Sets each entries onClick to be onItemClick in
     * SearchScreenActivity. onItemClick, in turn, sets the view to be entry_view, which displays
     * all the data for the entry clicked by the user.
     */
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View view) {
            SearchScreenActivity searchScreenActivity = (SearchScreenActivity) activity;
            searchScreenActivity.onEntryClick(mPosition);
        }
    }
}