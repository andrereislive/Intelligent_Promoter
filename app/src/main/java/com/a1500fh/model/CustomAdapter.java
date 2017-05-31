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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 25/05/2017.
 */

public class CustomAdapter extends ArrayAdapter<Produto> implements View.OnClickListener {

    private List<Produto> dataSet;
    Context mContext;
    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {
        TextView txtId;
        TextView txtNome;
    }


    public CustomAdapter(List<Produto> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }


    @Override
    public void onClick(View v) {


        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Produto dataModel = (Produto) object;


        switch (v.getId()) {

            case R.id.item_info:

//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();

                break;


        }


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Produto dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtId = (TextView) convertView.findViewById(R.id.id);
            viewHolder.txtNome = (TextView) convertView.findViewById(R.id.nome);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtId.setText(dataModel.getId().toString());
        viewHolder.txtNome.setText(dataModel.getNome());

        // Return the completed view to render on screen
        return convertView;
    }

}