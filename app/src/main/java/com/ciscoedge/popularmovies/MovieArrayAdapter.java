package com.ciscoedge.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class MovieArrayAdapter extends ArrayAdapter<Movie>{


        private Context context;
        private List<Movie> objects;


        public MovieArrayAdapter(Context context, int resource, List<Movie> objects) {
            super(context, resource, objects);

            this.context = context;
            this.objects = objects;

        }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }
        ImageView myMovie_Image_View = (ImageView) convertView.findViewById(R.id.movie_image);

        Picasso mPicasso = Picasso.with(context);
        mPicasso.setIndicatorsEnabled(true);
        mPicasso.load("http://image.tmdb.org/t/p/w185" + movie.image_location).into(myMovie_Image_View);

        return convertView;
    }
}
