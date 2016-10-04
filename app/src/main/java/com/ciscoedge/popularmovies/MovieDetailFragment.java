package com.ciscoedge.popularmovies;


import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Date;



public class MovieDetailFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {

            // if there is no containing frame we do not attempt to create the view
            // we simply exit by returning null.
            return null;
        }

        View detail_view = inflater.inflate(R.layout.fragment_detail_view, container, false);




        TextView movie_title = (TextView) detail_view.findViewById(R.id.txt_movie_title);
        movie_title.setText(getMovie().title);




        ImageView movie_image = (ImageView) detail_view.findViewById(R.id.img_movie);

        Picasso.with(movie_image.getContext())
                .load("http://image.tmdb.org/t/p/w185" + (getMovie().image_location))
                .into(movie_image);



        Date  movie_release_date;
        movie_release_date = Date.valueOf(getMovie().release_date);

        String year = (String) DateFormat.format("yyyy", movie_release_date);
        TextView my_release_year = (TextView) detail_view.findViewById(R.id.txt_movie_year);

        my_release_year.setText(year);




        String rating = getMovie().user_rating + "/10";

        TextView my_user_rating = (TextView) detail_view.findViewById(R.id.movie_rating);

        my_user_rating.setText(rating);




        String overview = getMovie().overview;

        TextView my_movie_overview = (TextView) detail_view.findViewById(R.id.movie_overview);

        my_movie_overview.setText(overview);




        return detail_view;
    }

    public Movie getMovie() {

        // the getArguments() returns a Bundle, upon which we can call
        //  the getParcelable method which returns a movie object.


        return getArguments().getParcelable("movie_parcel");
    }


    public static MovieDetailFragment createFragment(Movie movie) {
        MovieDetailFragment myDetailFragment = new MovieDetailFragment();

        //This creates a Bundle object that can hold
        //a mapping from String keys to various values
        Bundle args = new Bundle();

        // we inserts "a movie object into the mapping of this Bundle, replacing
        // any existing value for the index key.
        args.putParcelable("movie_parcel", movie);


        // The setArgument supplies a bundled set or arguments for myDetailFragment
        // As per android documentation, The setArgument() method can only
        // be called before the fragment has been attached to its activity.
        myDetailFragment.setArguments(args);

        // Here we return to the calling party our bundled fragment.
        return myDetailFragment;
    }

}
