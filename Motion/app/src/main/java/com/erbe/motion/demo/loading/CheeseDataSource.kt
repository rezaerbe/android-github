package com.erbe.motion.demo.loading

import android.os.SystemClock
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.erbe.motion.model.Cheese

class CheeseDataSource : PositionalDataSource<Cheese>() {

    companion object Factory : DataSource.Factory<Int, Cheese>() {
        override fun create(): DataSource<Int, Cheese> = CheeseDataSource()
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Cheese>) {
        // Simulate a slow network.
        SystemClock.sleep(3000L)
        callback.onResult(
            Cheese.ALL.subList(
                params.requestedStartPosition,
                params.requestedStartPosition + params.requestedLoadSize
            ),
            params.requestedStartPosition,
            Cheese.ALL.size
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Cheese>) {
        // Simulate a slow network.
        SystemClock.sleep(3000L)
        callback.onResult(
            Cheese.ALL.subList(
                params.startPosition, params.startPosition + params.loadSize
            )
        )
    }
}
