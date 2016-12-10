package com.ciscoedge.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ciscoedge.popularmovies.data.MovieDataSource;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.AdapterView.OnItemClickListener;


public class MovieDetailFragment extends Fragment implements OnLoadDataListener {
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private static final String MOVIE_SHARE_HASHTAG = " #PopularMoviesApp";
    private ShareActionProvider myShareActionProvider;

    AppTask mAsynchTask;
    AppTask mAsynchTask2;

    VideoArrayAdapter myVideoAdapter;
    ReviewArrayAdapter myReviewAdapter ;

    ExpandableHeightListView expandableListView;
    ExpandableHeightListView expandableListView2;

    LinearLayout llVideo;
    LinearLayout llReview;


    @Override
    public void onResume() {
        super.onResume();

        mAsynchTask = new AppTask(this.getActivity());
        mAsynchTask.setListener(this);

        try {
            mAsynchTask.execute(getMovie().movieId, "videos");

        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        mAsynchTask2 = new AppTask(this.getActivity());
        mAsynchTask2.setListener(this);

        try {
            mAsynchTask2.execute(getMovie().movieId, "reviews");

        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }


    @Override
    public void onLoadComplete(List<Object> data) {


        String objectType = null;


        if (data != null) {
            myReviewAdapter.clear();

            // This code populate our adapter with new data.
            for (Object objectList : data) {
                if(objectList instanceof Video) {

                    Video myVideo = (Video) objectList;
                    myVideoAdapter.add(myVideo);
                    objectType = "video" ;
                }else if(objectList instanceof Review) {

                    Review myReview = (Review)objectList;
                    myReviewAdapter.add(myReview);
                    objectType = "review" ;
                }
            }


            //if(objectType != null && objectType.equals("video")){

                if(myVideoAdapter != null && myVideoAdapter.getCount() != 0) {
                    if ( llVideo != null ){
                        llVideo.setVisibility(VISIBLE);
                    }
                }else {
                    if(llVideo != null) {
                        llVideo.setVisibility(GONE);
                    }
                }
            //}else if(objectType.equals("review")){

                if(myReviewAdapter != null  && myReviewAdapter.getCount() != 0) {
                    if ( llReview != null ) {
                        llReview.setVisibility(VISIBLE);
                    }
                }else {
                    if ( llReview != null ) {
                        llReview.setVisibility(GONE);
                    }
                }
            //}
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Populate Video list
        myVideoAdapter =
                new VideoArrayAdapter(getActivity(),
                        R.layout.review_item,
                        new ArrayList<Object>());

        // Populate Review list
        myReviewAdapter =
                new ReviewArrayAdapter(getActivity(),
                        R.layout.review_item,
                        new ArrayList<Object>());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            // if there is no containing frame we do not attempt to create the view
            // we simply exit by returning null.
            return null;
        }


        View detail_view = inflater.inflate(R.layout.fragment_list_view, container, false);

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

        CheckBox chkFavorite = (CheckBox) detail_view.findViewById(R.id.favorite_checkbox);

        if (getMovie().favorite.equals("true")){

            chkFavorite.setChecked(true);
        }

        chkFavorite.setOnClickListener(new View.OnClickListener()
        {
            Toast toast =  null;
            MovieDataSource dataSource = new MovieDataSource(getActivity().getApplicationContext());

            int result;

            @Override
            public void onClick(View v)
            {
                Movie myMovie = getMovie();
                CheckBox chkFavorite = (CheckBox) v.findViewById(R.id.favorite_checkbox);
                boolean isChecked = chkFavorite.isChecked();
                switch (isChecked+"") {
                    case "true":
                        toast = Toast.makeText(getActivity().getApplicationContext(), "The Movie " + getMovie().title + " was added to your favorite movie collection.", Toast.LENGTH_LONG);
                        getMovie().favorite = "true";
                        result = dataSource.updateMovie(myMovie);

                        if (toast != null && result > 0) {
                            toast.show();
                        }
                        break;
                    case "false":
                        toast = Toast.makeText(getActivity().getApplicationContext(), "The Movie " + getMovie().title + " was removed from your favorite movie collection.", Toast.LENGTH_LONG);
                        myMovie.favorite = "false";
                        int result = dataSource.updateMovie(myMovie);

                        if (toast != null && result > 0) {
                            toast.show();
                        }
                        break;
                    default:
                        toast = Toast.makeText(getActivity().getApplicationContext(), "ERROR - NO MOVIE ADDED!", Toast.LENGTH_LONG);

                         if (toast != null) {
                            toast.show();
                        }
                        break;
                }
            }
        });

        ExpandableHeightListView myVideoListView  = (ExpandableHeightListView) detail_view.findViewById(R.id.expandable_listview_video);

        myVideoListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                Video videoData = (Video)myVideoAdapter.getItem(position);
                String key = videoData.video_key;

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
            }
        });

        expandableListView2 = (ExpandableHeightListView) detail_view.findViewById(R.id.expandable_listview_video);
        expandableListView2.setAdapter(myVideoAdapter);
        expandableListView2.setExpanded(true);
        llVideo = (LinearLayout) detail_view.findViewById(R.id.llVideos);

        expandableListView = (ExpandableHeightListView)  detail_view.findViewById(R.id.expandable_listview_review);
        expandableListView.setAdapter(myReviewAdapter);
        expandableListView.setExpanded(true);
        llReview = (LinearLayout) detail_view.findViewById(R.id.llReviews);

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
