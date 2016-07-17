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

package org.mythtv.android.data.net;

import org.joda.time.DateTime;
import org.mythtv.android.data.entity.EncoderEntity;
import org.mythtv.android.data.entity.ProgramEntity;
import org.mythtv.android.data.entity.TitleInfoEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by dmfrey on 8/27/15.
 */
public interface DvrApi {

    String TITLE_INFO_LIST_URL = "/Dvr/GetTitleInfoList";

    String RECORDED_LIST_BASE_URL = "/Dvr/GetRecordedList";
    String RECORDED_BASE_URL = "/Dvr/GetRecorded?ChanId=%s&StartTime=%s";
    String UPCOMING_LIST_BASE_URL = "/Dvr/GetUpcomingList";
    String ENCODER_LIST_BASE_URL = "/Dvr/GetEncoderList";
    String UPDATE_RECORDED_WATCHED_STATUS_URL = "/Dvr/UpdateRecordedWatchedStatus";

    String DESCENDING_QS = "Descending=%s";
    String START_INDEX_QS = "StartIndex=%s";
    String COUNT_QS = "Count=%s";
    String TITLE_REG_EX_QS = "TitleRegEx=%s";
    String REC_GROUP_QS = "RecGroup=%s";
    String STORAGE_GROUP_QS = "StorageGroup=%s";
    String SHOW_ALL_QS = "ShowAll=%s";
    String RECORD_ID_QS = "RecordId=%s";
    String REC_STATUS_QS = "RecStatus=%s";
    String WATCHED_QS = "Watched=%s";

    Observable<List<TitleInfoEntity>> titleInfoEntityList();

    Observable<List<ProgramEntity>> recordedProgramEntityList( final boolean descending, final int startIndex, final int count, final String titleRegEx, final String recGroup, final String storageGroup );

    Observable<ProgramEntity> recordedProgramById( final int chanId, final DateTime startTime );

    Observable<List<ProgramEntity>> upcomingProgramEntityList( final int startIndex, final int count, final boolean showAll, final int recordId, final int recStatus );

    Observable<List<EncoderEntity>> encoderEntityList();

    /**
     * Updates Recording Watched status on the backend
     *
     * HTTP Status Codes:
     *   200 - if request completed successfully
     *   404 - if input parameters are valid, but recording is not found on the backend
     *   501 - if input parameters are invalid
     *
     * @return true  - if status was successfully updated
     *         false - if status was not updated
     */
    Observable<Boolean> updateWatchedStatus(final int chanId, final DateTime startTime, final boolean watched );

}