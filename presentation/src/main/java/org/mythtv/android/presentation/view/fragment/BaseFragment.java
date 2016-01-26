package org.mythtv.android.presentation.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import org.mythtv.android.domain.SettingsKeys;
import org.mythtv.android.presentation.internal.di.HasComponent;

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 *
 * Created by dmfrey on 8/30/15.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setRetainInstance( true );

    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param view the parent view
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage( View view, String message, String retryMessage, View.OnClickListener retryOnClickListener ) {

//        Toast.makeText( getActivity(), message, Toast.LENGTH_SHORT ).show();
        Snackbar
                .make( getView(), message, Snackbar.LENGTH_LONG )
                .setAction( retryMessage, retryOnClickListener )
                .show();

    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings( "unchecked" )
    protected <C> C getComponent( Class<C> componentType ) {

        return componentType.cast( ( (HasComponent<C>) getActivity() ).getComponent() );
    }

    protected String getMasterBackendUrl() {

        String host = getFromPreferences( getActivity(), SettingsKeys.KEY_PREF_BACKEND_URL );
        String port = getFromPreferences( getActivity(), SettingsKeys.KEY_PREF_BACKEND_PORT );

        String masterBackend = "http://" + host + ":" + port;

        return masterBackend;
    }

    protected String getFromPreferences( Context context, String key ) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(key, "");
    }

}
