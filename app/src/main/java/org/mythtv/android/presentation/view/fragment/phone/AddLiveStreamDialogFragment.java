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

package org.mythtv.android.presentation.view.fragment.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import org.mythtv.android.R;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 4/21/16.
 */
public class AddLiveStreamDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder
                .setTitle( R.string.add_live_stream_title )
                .setPositiveButton( R.string.add_live_stream_positive_label, (dialog, which) -> getTargetFragment().onActivityResult( getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent() ) )
                .setNegativeButton( android.R.string.cancel, (dialog, which) -> getTargetFragment().onActivityResult( getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent() ) );

        return builder.create();
    }

}
