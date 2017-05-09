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

package org.mythtv.android.data.repository.datasource;

import org.mythtv.android.data.entity.LiveStreamInfoEntity;
import org.mythtv.android.domain.Media;

import java.util.List;

import rx.Observable;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 10/17/15.
 */
public interface ContentDataStore {

    Observable<List<LiveStreamInfoEntity>> liveStreamInfoEntityList( final String filename );

    Observable<LiveStreamInfoEntity> addLiveStream( final Media media, final int id );

    Observable<Boolean> removeLiveStream( final int id );

}
