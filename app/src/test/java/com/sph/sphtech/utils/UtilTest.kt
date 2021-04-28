package com.sph.sphtech.utils

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.SmallTest
import com.sp.sphtech.utils.dc
import com.sp.sphtech.utils.dd
import com.sp.sphtech.utils.dm
import com.sp.sphtech.utils.progressView
import com.sp.sphtech.view.BarPercentView
import com.sph.sphtech.BaseRobolectricTest
import com.sph.sphtech.R
import org.jetbrains.anko.*
import org.junit.Assert
import org.junit.Test

@SmallTest
class UtilTest : BaseRobolectricTest() {

    /**
     *  test Util Extension Method
     */
    @Test
    fun testExtensionMethod() {
        Assert.assertEquals(
            context.resources.getDimension(R.dimen.margin_10).toInt(),
            R.dimen.margin_10.dm
        )
        Assert.assertEquals(context.resources.getColor(R.color.white), R.color.white.dc)
        Assert.assertTrue(R.mipmap.close.dd is Drawable)
    }

    /**
     * test dsl extension
     */
    @Test
    fun testDslExtensionMethod() {
        var ui = UI()
        Assert.assertTrue(
            ui.createView(
                AnkoContext.Companion.create(context, FrameLayout(context))
            ) is BarPercentView
        )
    }

    class UI : AnkoComponent<ViewGroup> {
        lateinit var root: BarPercentView
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            root = progressView {
            }
            root
        }

    }
}
