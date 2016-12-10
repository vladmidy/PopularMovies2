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


public class VideoArrayAdapter extends ArrayAdapter<Object>{


    private Context context;
    private List<Object> objects;


    public VideoArrayAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);

        this.context = context;
        this.objects = objects;
    }

    public static final int REGULAR_VIEW = 0;
    public static final int LAST_ITEM_VIEW = 1;

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        int size = objects.size();

        if(size == position + 1){

            return LAST_ITEM_VIEW;

        }else {

            return REGULAR_VIEW;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Video video = (Video)getItem(position);
        int type = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();

                if ( type == REGULAR_VIEW) {
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.video_item, parent, false);
                    holder.mMaterialIconView = (MaterialIconView)
                            convertView.findViewById(R.id.imgVideo);
                    holder.myVideoTextView = (TextView)
                            convertView.findViewById(R.id.txtVideo);

                }else if ( type == LAST_ITEM_VIEW) {
                    convertView = LayoutInflater.from(getContext()).inflate(
                            R.layout.last_video_item, parent, false);
                    holder.mMaterialIconView = (MaterialIconView)
                            convertView.findViewById(R.id.imgVideo);
                    holder.myVideoTextView = (TextView)
                            convertView.findViewById(R.id.txtVideo);
                }

            holder.mMaterialIconView.setColor(Color.BLACK );
            holder.mMaterialIconView.setIcon(MaterialDrawableBuilder.IconValue.PLAY);
            holder.myVideoTextView.setText(video.type);
            }

        return convertView;
    }


    public static class ViewHolder {

        public MaterialIconView mMaterialIconView;
        public TextView myVideoTextView;
    }
}
