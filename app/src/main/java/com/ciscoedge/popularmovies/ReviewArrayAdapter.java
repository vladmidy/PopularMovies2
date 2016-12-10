package com.ciscoedge.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;


public class ReviewArrayAdapter extends ArrayAdapter<Object>{


    private Context context;
    private List<Object> objects;


    public ReviewArrayAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);

        this.context = context;
        this.objects = objects;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = (Review)getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.review_item, parent, false);
        }

        MaterialIconView mMaterialIconView = (MaterialIconView) convertView.findViewById(R.id.imgReview);
        TextView myReviewTextView = (TextView) convertView.findViewById(R.id.txtReview) ;
        TextView myAuthorTextView = (TextView) convertView.findViewById(R.id.txtAuthor);

        mMaterialIconView.setColor(Color.GRAY );

        mMaterialIconView.setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT_BOX);

        myReviewTextView.setText(review.content);

        myAuthorTextView.setText("Contributors: "+ review.author);

        return convertView;

    }
}











