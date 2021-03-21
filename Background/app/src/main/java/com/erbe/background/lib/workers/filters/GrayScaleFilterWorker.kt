package com.erbe.background.lib.workers.filters

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import androidx.work.WorkerParameters
import com.erbe.background.ScriptC_grayscale

class GrayScaleFilterWorker(context: Context, parameters: WorkerParameters) :
    BaseFilterWorker(context, parameters) {

    override fun applyFilter(input: Bitmap): Bitmap {
        var rsContext: RenderScript? = null
        return try {
            rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
            val inAlloc = Allocation.createFromBitmap(rsContext, input)
            val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)
            // The Renderscript function that computes gray scale pixel values is defined in
            // `src/main/rs/grayscale.rs`. We compute a new pixel value for every pixel which is
            // out = (r + g + b) / 3 where r, g, b are the red, green and blue channels in the
            // input image.
            ScriptC_grayscale(rsContext).run {
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
