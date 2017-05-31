package com.a1500fh.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.a1500fh.intelligent_promoter.R;

import java.util.List;

/**
 * Created by Andre on 31/05/2017.
 */

public class CustomAdapterShare extends ArrayAdapter<String> implements View.OnClickListener {

    private List<String> dataSet;
    Context mContext;
    private int lastPosition = -1;


    public CustomAdapterShare(List<String> data, Context context) {
        super(context, R.layout.row_item_share, data);
        this.dataSet = data;
        this.mContext = context;

    }


    // View lookup cache
    private static class ViewHolder {

        TextView txtShare;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapterShare.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new CustomAdapterShare.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_share, parent, false);
            viewHolder.txtShare = (TextView) convertView.findViewById(R.id.share_desc);



            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapterShare.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtShare.setText(dataModel);


        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
