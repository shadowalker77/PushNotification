package ir.ayantech.pushnotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yokeyword.fragmentation.SwipeBackLayout
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment

abstract class FragmentationFragment : SwipeBackFragment() {
    var rootView: View? = null
    var shouldAttach = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        if (rootView != null)
//            return rootView
        rootView = inflater.inflate(getLayoutId(), container, false)
        setEdgeLevel(SwipeBackLayout.EdgeLevel.MIN)
        swipeBackLayout.setEdgeOrientation(SwipeBackLayout.EDGE_RIGHT)
        swipeBackLayout.setParallaxOffset(0f)
        if (shouldAttach) rootView = attachToSwipeBack(rootView)

        return rootView
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        onCreate()
    }

    protected abstract fun getLayoutId(): Int

    protected open fun onCreate() {
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultHorizontalAnimator()
    }

    abstract fun getPageTitle(): String
}