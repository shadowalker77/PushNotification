package ir.ayantech.pushnotification

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun RecyclerView.linearSetup() {
    this.layoutManager = LinearLayoutManager(context)
//    val divider = DividerItemDecorator(resources.getDrawable(R.drawable.divider))
//    val divider = DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.divider))
//    this.addItemDecoration(divider)
}

fun RecyclerView.linearSetupWithDivider() {
    this.layoutManager = LinearLayoutManager(context)
}

fun RecyclerView.horizontalLinearLayoutManagerSetup() {
    this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
}

fun RecyclerView.verticalLinearLayoutManagerSetup() {
    this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
}

//fun RecyclerView.gridViewLayoutManagerSetup() {
//    this.layoutManager = RtlGridLayoutManager(context, 6)
//}
//
//fun RecyclerView.rtlSetup(itemCount: Int = 1) {
//    layoutManager = RtlGridLayoutManager(context, itemCount, RecyclerView.VERTICAL, false)
//}

fun RecyclerView.onScrollToBottom(callBack: (lastPosition: Int) -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0)
                this@onScrollToBottom.layoutManager.let {
                    when (it) {
                        is LinearLayoutManager -> callBack(it.findLastVisibleItemPosition())
                        is GridLayoutManager -> callBack(it.findLastVisibleItemPosition())
                        is StaggeredGridLayoutManager -> {
                            // TODO
                        }
                    }
                }
        }
    })
}