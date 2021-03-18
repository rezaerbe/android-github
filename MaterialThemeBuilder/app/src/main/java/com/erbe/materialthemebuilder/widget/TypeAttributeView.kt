package com.erbe.materialthemebuilder.widget

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import com.erbe.materialthemebuilder.R

/**
 * A composite view to display a text label and a preview of a TextAppearance theme attribute.
 */
class TypeAttributeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val typeAttributeTextView: AppCompatTextView
    private val typeAttributePreviewTextView: AppCompatTextView

    private var typeAttrText: String = "?textAppearanceHeadline1"
        set(value) {
            typeAttributeTextView.text = value
            field = value
        }

    var typeAttrPreviewText: String = context.getString(R.string.text_appearance_h1_label)
        set(value) {
            typeAttributePreviewTextView.text = value
            field = value
        }

    private var typeAttrPreviewTextAppearance: Int = R.attr.textAppearanceHeadline1
        @RequiresApi(Build.VERSION_CODES.M)
        set(value) {
            typeAttributePreviewTextView.setTextAppearance(value)
            field = value
        }

    private var typeAttrPreviewTextColor: ColorStateList = AppCompatResources.getColorStateList(
        context,
        R.color.material_on_background_emphasis_high_type
    )
        set(value) {
            typeAttributePreviewTextView.setTextColor(value)
            field = value
        }

    init {
        orientation = HORIZONTAL
        val view = View.inflate(context, R.layout.type_attribute_view_layout, this)
        typeAttributeTextView = view.findViewById(R.id.type_attribute)
        typeAttributePreviewTextView = view.findViewById(R.id.type_attribute_preview)

        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TypeAttributeView,
            defStyleAttr,
            defStyleRes
        )
        typeAttrText = a.getString(R.styleable.TypeAttributeView_android_text) ?: typeAttrText
        typeAttrPreviewText = a.getString(
            R.styleable.TypeAttributeView_previewText
        ) ?: typeAttrPreviewText
        typeAttrPreviewTextAppearance = a.getResourceId(
            R.styleable.TypeAttributeView_previewTextAppearance,
            typeAttrPreviewTextAppearance
        )
        typeAttrPreviewTextColor = a.getColorStateList(
            R.styleable.TypeAttributeView_previewTextColor
        ) ?: typeAttrPreviewTextColor
        a.recycle()
    }
}
