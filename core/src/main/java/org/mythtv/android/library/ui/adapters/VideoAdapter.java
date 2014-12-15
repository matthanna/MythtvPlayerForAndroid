package org.mythtv.android.library.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mythtv.android.library.R;
import org.mythtv.android.library.core.domain.video.Video;

import java.util.List;

/**
 * Created by dmfrey on 11/29/14.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final String TAG = VideoAdapter.class.getSimpleName();

    private List<Video> videos;
    private VideoClickListener videoClickListener;

    public VideoAdapter( List<Video> videos, @NonNull VideoClickListener videoClickListener ) {
        Log.v( TAG, "initialize : enter" );

        this.videos = videos;
        this.videoClickListener = videoClickListener;

        Log.v( TAG, "initialize : exit" );
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int position ) {
        Log.v( TAG, "onCreateViewHolder : enter" );

        View v = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.video_list_item, viewGroup, false );

        ViewHolder vh = new ViewHolder( v );
        return vh;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int position ) {
        Log.v( TAG, "onBindViewHolder : enter" );

        final Video video = videos.get( position );
        viewHolder.setTitle( video.getTitle() );
        viewHolder.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                videoClickListener.videoClicked( video );
            }

        });

        Log.v( TAG, "onBindViewHolder : exit" );
    }

    @Override
    public int getItemCount() {
        Log.v( TAG, "getItemCount : enter" );

        Log.v( TAG, "getItemCount : exit" );
        return videos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static String TAG = ViewHolder.class.getSimpleName();

        private final View parent;
        private final TextView title;

        public ViewHolder( View v ) {
            super( v );
            Log.v( TAG, "initialize : enter" );

            this.parent = v;
            title = (TextView) parent.findViewById( R.id.video_item_title );

            Log.v( TAG, "initialize : exit" );
        }

        public void setTitle( CharSequence text ) {

            title.setText( text );

        }

        public void setOnClickListener( View.OnClickListener listener ) {

            parent.setOnClickListener( listener );

        }

    }

    public interface VideoClickListener {

        void videoClicked( Video video );

    }

}