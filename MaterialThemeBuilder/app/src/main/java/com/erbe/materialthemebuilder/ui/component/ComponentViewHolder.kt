package com.erbe.materialthemebuilder.ui.component

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.erbe.materialthemebuilder.R
import com.erbe.materialthemebuilder.ui.component.Component.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

/**
 * Sealed class to define all [RecyclerView.ViewHolder]s used to display [Component]s.
 */
sealed class ComponentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(component: Component) {
        // Override in subclass if needed.
    }

    class ButtonComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_buttons))

    class FabComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_fabs))

    class CardComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_cards))

    class TopAppBarComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_top_app_bar))

    class ChipComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_chips))

    class DrawerComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_drawer)) {
        private val drawerLayout: DrawerLayout = view.findViewById(R.id.drawer_layout)
        private val navigationView: NavigationView = view.findViewById(R.id.nav_view)

        override fun bind(component: Component) {
            drawerLayout.openDrawer(GravityCompat.START)
            navigationView.setNavigationItemSelectedListener { true }
            navigationView.setCheckedItem(R.id.nav_item_one)
        }
    }

    class TextFieldComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_text_field))

    class BottomNavigationComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_bottom_navigation))

    class SwitchComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_switch))

    class RadioButtonComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_radio_button))

    class CheckboxComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_checkbox))

    class BottomAppBarComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_bottom_app_bar)) {
        private val bottomAppBar: BottomAppBar = view.findViewById(R.id.bottom_app_bar)

        override fun bind(component: Component) {
            bottomAppBar.overflowIcon = ContextCompat.getDrawable(
                bottomAppBar.context,
                R.drawable.ic_more_vert_on_surface_24dp
            )
        }
    }

    class TabsComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_tabs))

    @SuppressLint("ShowToast")
    class SnackbarComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_snackbar)) {

        init {
            val container: FrameLayout = view.findViewById(R.id.snackbar_container)
            val snackbarView = Snackbar.make(
                container,
                R.string.snackbar_message_text,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.snackbar_action_text) { }
                .view
            (snackbarView.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER

            container.addView(snackbarView)
        }
    }

    class DialogComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_dialog)) {
        init {
            val button = view.findViewById<MaterialButton>(R.id.button)
            button.setOnClickListener {
                showDialog()
            }
        }

        private fun showDialog() {
            MaterialAlertDialogBuilder(view.context)
                .setTitle(R.string.text_headline_6)
                .setMessage(R.string.lorem_ipsum)
                .setPositiveButton(R.string.text_button, null)
                .setNegativeButton(R.string.text_button, null)
                .show()
        }
    }

    class BottomSheetComponentViewHolder(
        parent: ViewGroup,
        listener: ComponentAdapter.ComponentAdapterListener
    ) : ComponentViewHolder(inflate(parent, R.layout.component_bottom_sheet)) {
        init {
            view.findViewById<MaterialButton>(R.id.button).setOnClickListener {
                listener.onShowBottomSheetClicked()
            }
        }
    }

    class ImageComponentViewHolder(
        parent: ViewGroup
    ) : ComponentViewHolder(inflate(parent, R.layout.component_image))

    companion object {
        fun create(
            parent: ViewGroup,
            viewType: Int,
            listener: ComponentAdapter.ComponentAdapterListener
        ): ComponentViewHolder {
            return when (Component.values()[viewType]) {
                BUTTON -> ButtonComponentViewHolder(parent)
                FAB -> FabComponentViewHolder(parent)
                CARD -> CardComponentViewHolder(parent)
                TOP_APP_BAR -> TopAppBarComponentViewHolder(parent)
                CHIP -> ChipComponentViewHolder(parent)
                DRAWER -> DrawerComponentViewHolder(parent)
                TEXT_FIELD -> TextFieldComponentViewHolder(parent)
                BOTTOM_NAVIGATION -> BottomNavigationComponentViewHolder(parent)
                SWITCH -> SwitchComponentViewHolder(parent)
                RADIO_BUTTON -> RadioButtonComponentViewHolder(parent)
                CHECKBOX -> CheckboxComponentViewHolder(parent)
                BOTTOM_APP_BAR -> BottomAppBarComponentViewHolder(parent)
                TABS -> TabsComponentViewHolder(parent)
                SNACKBAR -> SnackbarComponentViewHolder(parent)
                DIALOG -> DialogComponentViewHolder(parent)
                BOTTOM_SHEET -> BottomSheetComponentViewHolder(parent, listener)
                IMAGE -> ImageComponentViewHolder(parent)
            }
        }

        private fun inflate(parent: ViewGroup, layout: Int): View {
            return LayoutInflater.from(parent.context).inflate(layout, parent, false)
        }
    }
}
