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

package org.mythtv.android.domain.interactor;

import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 4/12/16.
 */
public abstract class DynamicUseCase extends UseCase {

    protected DynamicUseCase( ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {
        super( threadExecutor, postExecutionThread );
    }

    protected abstract Observable buildUseCaseObservable( Map parameters );

    @Override
    protected Observable buildUseCaseObservable() {

        throw new UnsupportedOperationException( "Use buildUseCaseObservable( Map parameters )" );
    }

    @SuppressWarnings( "unchecked" )
    public void execute( Subscriber UseCaseSubscriber, Map parameters ) {
        this.subscription = this.buildUseCaseObservable( parameters )
                .subscribeOn( Schedulers.from( threadExecutor ) )
                .observeOn( postExecutionThread.getScheduler() )
                .subscribe( UseCaseSubscriber );
    }

}
