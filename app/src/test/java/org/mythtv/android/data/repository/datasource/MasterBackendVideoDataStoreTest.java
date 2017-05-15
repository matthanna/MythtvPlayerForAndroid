package org.mythtv.android.data.repository.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mythtv.android.data.ApplicationTestCase;
import org.mythtv.android.data.entity.VideoMetadataInfoEntity;
import org.mythtv.android.data.net.VideoApi;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by dmfrey on 11/12/15.
 */
public class MasterBackendVideoDataStoreTest extends ApplicationTestCase {

    private MasterBackendVideoDataStore masterBackendVideoDataStore;

    @Mock
    private VideoApi mockVideoApi;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks( this );
        masterBackendVideoDataStore = new MasterBackendVideoDataStore( mockVideoApi );

    }

    @Test
    public void whenGetVideoList_verifyVideoListReturned() {

        given( mockVideoApi.getVideoList( anyString(), anyString(), anyBoolean(), anyInt(), anyInt() ) ).willReturn( setupVideos() );

        Observable<List<VideoMetadataInfoEntity>> observable = masterBackendVideoDataStore.getVideos( null, null, false, -1, -1 );
        TestSubscriber<List<VideoMetadataInfoEntity>> testSubscriber = new TestSubscriber<>();
        observable.subscribe( testSubscriber );

        assertThat( testSubscriber.getOnNextEvents().get( 0 ) )
                .isNotNull()
                .hasSize( 1 );

        verify( mockVideoApi ).getVideoList( anyString(), anyString(), anyBoolean(), anyInt(), anyInt() );

    }

    @Test
    public void whenGetCategory_verifyCategoryResults() {

        given( mockVideoApi.getVideoList( anyString(), anyString(), anyBoolean(), anyInt(), anyInt() ) ).willReturn( setupVideos() );

        Observable<List<VideoMetadataInfoEntity>> observable = masterBackendVideoDataStore.getCategory( FAKE_CONTENT_TYPE );
        TestSubscriber<List<VideoMetadataInfoEntity>> testSubscriber = new TestSubscriber<>();
        observable.subscribe( testSubscriber );

        assertThat( testSubscriber.getOnNextEvents().get( 0 ) )
                .isNotNull()
                .hasSize( 1 );

        verify( mockVideoApi ).getVideoList( anyString(), anyString(), anyBoolean(), anyInt(), anyInt() );

    }

    @Test
    public void whenGetVideoById_verifyVideoReturned() {

        given( mockVideoApi.getVideoById( anyInt() ) ).willReturn( setupVideo() );

        Observable<VideoMetadataInfoEntity> observable = masterBackendVideoDataStore.getVideoById( FAKE_ID );
        TestSubscriber<VideoMetadataInfoEntity> testSubscriber = new TestSubscriber<>();
        observable.subscribe( testSubscriber );

        assertThat( testSubscriber.getOnNextEvents().get( 0 ) )
                .isNotNull();

        verify( mockVideoApi ).getVideoById( anyInt() );

    }

    @Test
    public void whenUpdateWatchedStatus_verifyBooleanReturned() {

        given( mockVideoApi.updateWatchedStatus( anyInt(), anyBoolean() ) ).willReturn( setupWatched() );

        Observable<Boolean> observable = masterBackendVideoDataStore.updateWatchedStatus( 1, Boolean.TRUE );
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        observable.subscribe( testSubscriber );

        assertThat( testSubscriber.getOnNextEvents().get( 0 ) )
                .isNotNull()
                .isTrue();

        verify( mockVideoApi, times( 1 ) ).updateWatchedStatus( anyInt(), anyBoolean() );

    }

    private Observable<List<VideoMetadataInfoEntity>> setupVideos() {

        return Observable.just( Collections.singletonList( createFakeVideoMetadataInfoEntity() ) );
    }

    private Observable<VideoMetadataInfoEntity> setupVideo() {

        return Observable.just( createFakeVideoMetadataInfoEntity() );
    }

    private Observable<Boolean> setupWatched() {

        return Observable.just( Boolean.TRUE );
    }

}
