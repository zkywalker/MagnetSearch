package org.zky.tool.magnetsearch.utils

import android.animation.ObjectAnimator
import android.view.View


fun View.zoomIn() = ObjectAnimator.ofFloat(this, "scaleX", 0f, 1f)
        .apply {
            duration = 300
            addUpdateListener { anim ->
                this@zoomIn.scaleY = anim.animatedValue as Float
            }
        }.also {
            visibility = View.VISIBLE
        }.start()

fun View.zoomOut() = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
        .apply {
            duration = 300
            addUpdateListener { anim ->
                (anim.animatedValue as Float).let {
                    with(this@zoomOut) {
                        scaleY = it
                        if (it == 0f) visibility = View.GONE
                    }
                }
            }
        }.start()