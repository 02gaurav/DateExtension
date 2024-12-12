package com.gaurav.dateextension

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
/*DecorView
├── LinearLayout // true
│   ├── ViewStub
│   └── FrameLayout
│       └── FitWindowsLinearLayout
│           ├── ViewStubCompat
│           └── ContentFrameLayout
│               └── CoordinatorLayout
│                   ├── AppBarLayout
│                   │   └── Toolbar
│                   │       ├── AppCompatTextView
│                   │       └── ActionMenuView
│                   │           └── OverflowMenuButton
│                   ├── FloatingActionButton
│                   └── ConstraintLayout
│                       ├── AppCompatImageView
│                       ├── AppCompatImageView
│                       └── AppCompatTextView
├── View
└── View */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //assertEquals(4, 2 + 2)
        val appCompatTextView =  View("AppCompatTextView", null);
        val constraintLayout =  View("ConstraintLayout", listOf(appCompatTextView,appCompatTextView,appCompatTextView));
        val floatingActionButton =  View("FloatingActionButton", null);
        val overflowMenuButton =  View("OverflowMenuButton", null);
        val actionMenuView =  View("ActionMenuView", listOf(overflowMenuButton));
        val toolbar =  View("Toolbar", listOf(appCompatTextView,actionMenuView));
        val appBarLayout =  View("AppBarLayout", listOf(toolbar));
        val coordinatorLayout =  View("CoordinatorLayout", listOf(appBarLayout,floatingActionButton,constraintLayout));
        val contentFrameLayout =  View("ContentFrameLayout", listOf(coordinatorLayout));
        val viewStubCompat =  View("ViewStubCompat", null);
        val fitWindowsLinearLayout =  View("FitWindowsLinearLayout", listOf(viewStubCompat,contentFrameLayout));
        val frameLayout = View("FrameLayout", listOf(fitWindowsLinearLayout));
        val viewStub =  View("ViewStub", null);
        val linearLayout =  View("LinearLayout", listOf(viewStub,frameLayout));
        val view =  View("View", null);
        val decorView =  View("DecorView", listOf(linearLayout,view,view));
        solveViewHiararchy(decorView)
    }
}
data class View(val name:String, val child:List<View>?=null)

fun solveViewHiararchy(view:View, prefix: String = "", isLast: Boolean = true) {
    println("$prefix${if (isLast) "└── " else "├── "}${view.name}")
    val size = view.child?.size ?: 0
    for(i in 0 until size) {
        val last = i == size-1
        solveViewHiararchy(view.child!![i], "$prefix${if (isLast) "    " else "│   "}", last)
    }
}
