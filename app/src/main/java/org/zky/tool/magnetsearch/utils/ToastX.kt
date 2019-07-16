package org.zky.tool.magnetsearch.utils

import android.view.View

fun String.toastShort(){
    ToastUtils.showToastShort(this)
}

fun  Int.toastShort(){
    ToastUtils.showToastShort(this)
}

fun String.toastLong(){
    ToastUtils.showToastLong(this)
}

fun Int.toastLong(){
    ToastUtils.showToastLong(this)
}

fun String.snack(parent: View){
    ToastUtils.snack(parent,this)
}

fun Int.snack(parent: View){
    ToastUtils.snack(parent,this)
}