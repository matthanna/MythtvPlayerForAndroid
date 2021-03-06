/*
 * MythtvPlayerForAndroid. An application for Android users to play MythTV Recordings and Videos
 * Copyright (c) 2016. Daniel Frey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mythtv.android.presentation.view.activity.phone;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.mythtv.android.R;
import org.mythtv.android.presentation.internal.di.HasComponent;
import org.mythtv.android.presentation.internal.di.components.DaggerMediaComponent;
import org.mythtv.android.presentation.internal.di.components.MediaComponent;
import org.mythtv.android.presentation.internal.di.modules.SearchResultsModule;
import org.mythtv.android.presentation.model.MediaItemModel;
import org.mythtv.android.presentation.provider.MythtvSearchSuggestionProvider;
import org.mythtv.android.presentation.view.fragment.phone.MediaItemSearchResultListFragment;
import org.mythtv.android.presentation.view.listeners.MediaItemListListener;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 10/14/15.
 */
public class SearchableActivity extends AbstractBasePhoneActivity implements HasComponent<MediaComponent>, MediaItemListListener {

    private static final String TAG = SearchableActivity.class.getSimpleName();

    private static final String INSTANCE_STATE_PARAM_SEARCH_TEXT = "org.mythtv.android.STATE_PARAM_SEARCH_TEXT";

    private String searchText;
    private MediaComponent mediaComponent;

    @Override
    public int getLayoutResource() {

        return R.layout.activity_phone_search_result_list;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        Log.d( TAG, "onCreate : enter" );
        super.onCreate( savedInstanceState );

        this.initializeActivity( getIntent() );
        this.initializeInjector();

        Log.d( TAG, "onCreate : exit" );
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        Log.d(TAG, "onSaveInstanceState : enter");

        if( null != outState ) {
            Log.d( TAG, "onSaveInstanceState : outState is not null" );

            if( null != this.searchText && !"".equals( searchText ) ) {

                outState.putString( INSTANCE_STATE_PARAM_SEARCH_TEXT, this.searchText );

            }

        }

        super.onSaveInstanceState( outState );

        Log.d( TAG, "onSaveInstanceState : exit" );
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState ) {
        super.onRestoreInstanceState( savedInstanceState );
        Log.d( TAG, "onRestoreInstanceState : enter" );

        if( null != savedInstanceState ) {
            Log.d( TAG, "onRestoreInstanceState : savedInstanceState != null" );

            if( savedInstanceState.containsKey( INSTANCE_STATE_PARAM_SEARCH_TEXT ) ) {

                this.searchText = savedInstanceState.getString( INSTANCE_STATE_PARAM_SEARCH_TEXT );

            }

        }

        Log.d( TAG, "onRestoreInstanceState : exit" );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {

        int i = item.getItemId();
        if( i == android.R.id.home ) {

            finish();

            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity( Intent intent ) {
        Log.d( TAG, "initializeActivity : enter" );

        if( null == intent  ) {
            Log.d( TAG, "initializeActivity : intent == null" );

            addFragment( R.id.fl_fragment, MediaItemSearchResultListFragment.newInstance( this.searchText ) );

        } else {
            Log.d( TAG, "initializeActivity : intent != null" );

            searchText = intent.getStringExtra( SearchManager.QUERY );
            Log.d( TAG, "initializeActivity : searchText = " + searchText );

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions( this, MythtvSearchSuggestionProvider.AUTHORITY, MythtvSearchSuggestionProvider.MODE );
            suggestions.saveRecentQuery( searchText, null );

            addFragment( R.id.fl_fragment, MediaItemSearchResultListFragment.newInstance( this.searchText ) );

        }

        Log.d( TAG, "initializeActivity : exit" );
    }

    private void initializeInjector() {
        Log.d( TAG, "initializeInjector : enter" );

        this.mediaComponent = DaggerMediaComponent.builder()
                .applicationComponent( getApplicationComponent() )
                .searchResultsModule( new SearchResultsModule() )
                .build();

        Log.d( TAG, "initializeInjector : exit" );
    }

    @Override
    public MediaComponent getComponent() {

        return mediaComponent;
    }

    @Override
    public void onMediaItemClicked( final MediaItemModel mediaItemModel, final View sharedElement, final String sharedElementName ) {
        Log.d( TAG, "onMediaItemClicked : enter" );

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation( this, sharedElement, sharedElementName );
        navigator.navigateToMediaItem( this, mediaItemModel.id(), mediaItemModel.media(), options );

        Log.d( TAG, "onMediaItemClicked : exit" );
    }

}
