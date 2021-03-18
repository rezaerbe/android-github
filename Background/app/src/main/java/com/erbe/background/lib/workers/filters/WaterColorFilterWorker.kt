package com.erbe.background.lib.workers.filters

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import androidx.work.WorkerParameters
import com.erbe.background.ScriptC_waterColorEffect

class WaterColorFilterWorker(context: Context, parameters: WorkerParameters) :
        BaseFilterWorker(context, parameters) {

    override fun applyFilter(input: Bitmap): Bitmap {
        var rsContext: RenderScript? = null
        return try {
            rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
            val inAlloc = Allocation.createFromBitmap(rsContext, input)
            val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)
            // The Renderscript function that generates the water color effect is defined in
            // `src/main/rs/waterColorEffect.rs`. The main idea, is to select a window of the image
            // and then find the most dominant pixel value. Then we set the r, g, b, channels of the
            // pixels to the one with the dominant pixel value.
            ScriptC_waterColorEffect(rsContext).run {
                _script = this
                _width = input.width.toLong()
                _height = input.height.toLong()
                _in = inAlloc
                _out = outAlloc
                invoke_filter()
            }
            Bitmap.createBitmap(input.width, input.height, input.config).apply {
                outAlloc.copyTo(this)
            }
        } finally {
            rsContext?.finish()
        }
    }
}
