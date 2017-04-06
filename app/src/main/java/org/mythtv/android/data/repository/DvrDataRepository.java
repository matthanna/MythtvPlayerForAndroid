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

package org.mythtv.android.data.repository;

import android.util.Log;

import org.mythtv.android.data.entity.LiveStreamInfoEntity;
import org.mythtv.android.data.entity.ProgramEntity;
import org.mythtv.android.data.entity.mapper.EncoderEntityDataMapper;
import org.mythtv.android.data.entity.mapper.MediaItemDataMapper;
import org.mythtv.android.data.entity.mapper.SeriesDataMapper;
import org.mythtv.android.data.repository.datasource.ContentDataStore;
import org.mythtv.android.data.repository.datasource.ContentDataStoreFactory;
import org.mythtv.android.data.repository.datasource.DvrDataStore;
import org.mythtv.android.data.repository.datasource.DvrDataStoreFactory;
import org.mythtv.android.domain.Encoder;
import org.mythtv.android.domain.MediaItem;
import org.mythtv.android.domain.Series;
import org.mythtv.android.domain.repository.DvrRepository;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 8/27/15.
 */
@Singleton
public class DvrDataRepository implements DvrRepository {

    private static final String TAG = DvrDataRepository.class.getSimpleName();
    private static final String CONVERT2METHODREF = "Convert2MethodRef";

    private final DvrDataStoreFactory dvrDataStoreFactory;
    private final ContentDataStoreFactory contentDataStoreFactory;

    @Inject
    public DvrDataRepository( DvrDataStoreFactory dvrDataStoreFactory, ContentDataStoreFactory contentDataStoreFactory ) {

        this.dvrDataStoreFactory = dvrDataStoreFactory;
        this.contentDataStoreFactory = contentDataStoreFactory;

    }

    @SuppressWarnings( CONVERT2METHODREF )
    @Override
    public Observable<List<Series>> titleInfos() {
        Log.d( TAG, "titleInfos : enter" );

        final DvrDataStore dvrDataStore = this.dvrDataStoreFactory.createMasterBackendDataStore();

        return dvrDataStore.titleInfoEntityList()
                .doOnError( throwable -> Log.e( TAG, "titleInfos : error", throwable ) )
                .map( titleInfoEntities -> SeriesDataMapper.transformPrograms( titleInfoEntities ) );
    }

    @SuppressWarnings( CONVERT2METHODREF )
    @Override
    public Observable<List<MediaItem>> recordedPrograms( boolean descending, int startIndex, int count, String titleRegEx, String recGroup, String storageGroup ) {
        Log.d( TAG, "recordedPrograms : enter" );

        Log.d( TAG, "recordedPrograms : descending=" + descending + ", startIndex=" + startIndex + ", count=" + count + ", titleRegEx=" + titleRegEx + ", recGroup=" + recGroup + ", storageGroup=" + storageGroup );

        final DvrDataStore dvrDataStore = this.dvrDataStoreFactory.createMasterBackendDataStore();
        final ContentDataStore contentDataStore = this.contentDataStoreFactory.createMasterBackendDataStore();

        Observable<List<ProgramEntity>> programEntities = dvrDataStore.recordedProgramEntityList( descending, startIndex, count, titleRegEx, recGroup, storageGroup );
        Observable<List<LiveStreamInfoEntity>> liveStreamInfoEntities = contentDataStore.liveStreamInfoEntityList( null );

        Observable<List<ProgramEntity>> recordedProgramEntityList = Observable.zip( programEntities, liveStreamInfoEntities, ( programEntityList, list ) -> {

            if( null != list && !list.isEmpty() ) {

                for( ProgramEntity programEntity : programEntityList ) {

                    for( LiveStreamInfoEntity liveStreamInfoEntity : list ) {

                        if( liveStreamInfoEntity.sourceFile().endsWith( programEntity.fileName() ) ) {

                            programEntity.setLiveStreamInfoEntity( liveStreamInfoEntity );

                        }

                    }

                }
            }

            return programEntityList;
        });

        return recordedProgramEntityList
                .map( recordedProgramEntities -> {
                    try {

                        return MediaItemDataMapper.transformPrograms( recordedProgramEntities );

                    } catch( UnsupportedEncodingException e ) {
                        Log.e( TAG, "recordedPrograms : error", e );
                    }

                    return new ArrayList<>();
                });
    }

    @SuppressWarnings( CONVERT2METHODREF )
    @Override
    public Observable<MediaItem> recordedProgram( final int recordedId ) {
        Log.d( TAG, "recordedProgram : enter" );

        Log.d( TAG, "recordedProgram : recordedId=" + recordedId );

        final DvrDataStore dvrDataStore = this.dvrDataStoreFactory.createMasterBackendDataStore();
        final ContentDataStore contentDataStore = this.contentDataStoreFactory.createMasterBackendDataStore();

        Observable<ProgramEntity> programEntity = dvrDataStore.recordedProgramEntityDetails( recordedId );
        Observable<List<LiveStreamInfoEntity>> liveStreamInfoEntity = programEntity
                .flatMap( entity -> contentDataStore.liveStreamInfoEntityList( entity.fileName() ) );
        Observable<Long> bookmark = dvrDataStore.getBookmark( recordedId, "Duration" );

        Observable<ProgramEntity> recordedProgramEntity = Observable.zip( programEntity, liveStreamInfoEntity, bookmark, ( programEntity1, liveStreamInfoEntityList, bookmark1 ) -> {

            if( null != liveStreamInfoEntityList && !liveStreamInfoEntityList.isEmpty() ) {

                programEntity1.setLiveStreamInfoEntity( liveStreamInfoEntityList.get( 0 ) );

            }

            programEntity1.setBookmark( bookmark1 );

            Log.d( TAG, "recordedProgram : programEntity=" + programEntity1.toString() );
            return programEntity1;
        });

        return recordedProgramEntity
                .doOnError( throwable -> Log.e( TAG, "recordedProgram : error", throwable ) )
                .map( recordedProgram  -> {
                    try {

                        return MediaItemDataMapper.transform( recordedProgram );

                    } catch( UnsupportedEncodingException e ) {
                        Log.e( TAG, "recordedProgram : error", e );
                    }

                    return null;
                });
    }

    @Override
    public Observable<List<MediaItem>> upcoming( int startIndex, int count, boolean showAll, int recordId, int recStatus ) {
        Log.d( TAG, "upcoming : enter" );
        Log.d( TAG, "upcoming : startIndex=" + startIndex + ", count=" + count + ", showAll=" + showAll + ", recordId=" + recordId + ", recStatus=" + recStatus );

        final DvrDataStore dvrDataStore = this.dvrDataStoreFactory.createMasterBackendDataStore();

        return dvrDataStore.upcomingProgramEntityList( startIndex, count, showAll, recordId, recStatus )
                .map( upcomingPrograms -> {
                    try {

                        return MediaItemDataMapper.transformPrograms(upcomingPrograms);

                    } catch( UnsupportedEncodingException e ) {
                        Log.e( TAG, "upcoming : error", e );
                    }

                    return new ArrayList<>();
                });
    }

    @SuppressWarnings( CONVERT2METHODREF )
    @Override
    public Observable<List<MediaItem>> recent() {
        Log.d( TAG, "recent : enter" );

        final DvrDataStore dvrDataStore = this.dvrDataStoreFactory.createMasterBackendDataStore();
        final ContentDataStore contentDataStore = this.contentDataStoreFactory.createMasterBackendDataStore();

        Observable<List<ProgramEntity>> programEntities = dvrDataStore.recordedProgramEntityList( true, 1, 50, null, null, null );
        Observable<List<LiveStreamInfoEntity>> liveStreamInfoEntities = contentDataStore.liveStreamInfoEntityList( null );

        Observable<List<ProgramEntity>> recordedProgramEntityList = Observable.zip( programEntities, liveStreamInfoEntities, ( programEntityList, list ) -> {

            if( null != list && !list.isEmpty() ) {

                for( ProgramEntity programEntity : programEntityList ) {

                    for( LiveStreamInfoEntity liveStreamInfoEntity : list ) {

                        if( liveStreamInfoEntity.sourceFile().endsWith( programEntity.fileName() ) ) {

                            programEntity.setLiveStreamInfoEntity( liveStreamInfoEntity );

                        }

                    }

                }
            }

            return programEntityList;
        });

        // Limit results to 50, then remove anything in the LiveTV storage group and only take 10 for the final results
        return recordedProgramEntityList
                .flatMap( Observable::from )
                .filter( programEntity -> !programEntity.recording().storageGroup().equalsIgnoreCase( "LiveTV" ) )
                .take( 10 )
                .toList()
                .map( recordedProgramEntities -> {
                    try {

                        return MediaItemDataMapper.transformPrograms( recordedProgramEntities );

                    } catch( UnsupportedEncodingException e ) {
                        Log.e( TAG, "recent : error", e );
                    }

                    return new ArrayList<>();
                });
    }

    @SuppressWarnings( CONVERT2METHODREF )
    @Override
    public Observable<List<Encoder>> encoders() {
        Log.d( TAG, "encoders : enter" );

        final DvrDataStore dvrDataStore = this.dvrDataStoreFactory.createMasterBackendDataStore();

        return dvrDataStore.encoderEntityList()
                .doOnError( throwable -> Log.e( TAG, "encoders : error", throwable ) )
                .map( encoderEntities -> EncoderEntityDataMapper.transformCollection( encoderEntities ) );
    }

    @SuppressWarnings( CONVERT2METHODREF )
    @Override
    public Observable<MediaItem> updateWatchedStatus( final int id, final boolean watched ) {
        Log.d( TAG, "updateWatchedStatus : enter" );

        final DvrDataStore dvrDataStore = this.dvrDataStoreFactory.createMasterBackendDataStore();

        Observable<Boolean> updateWatchedStatus = dvrDataStore.updateWatchedStatus( id, watched )
                .doOnError( throwable -> Log.e( TAG, "updateWatchedStatus : error", throwable ) )
                .doOnCompleted( () -> dvrDataStore.recordedProgramEntityList( true, -1, -1, null, null, null ) );

        Observable<MediaItem> recordedProgram = recordedProgram( id );

        return Observable.zip( updateWatchedStatus, recordedProgram, ( updateWatchedStatus1, recordedProgram1 ) -> recordedProgram1 );
    }

}
