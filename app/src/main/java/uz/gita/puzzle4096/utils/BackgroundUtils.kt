package uz.gita.puzzle4096.utils

import uz.gita.puzzle4096.R

object BackgroundUtils {
    val bgList = arrayOf(
        R.drawable.bg_item_0,
        R.drawable.bg_item_0,
        R.drawable.bg_item_2,
        R.drawable.bg_item_4,
        R.drawable.bg_item_8,
        R.drawable.bg_item_16,
        R.drawable.bg_item_32,
        R.drawable.bg_item_64,
        R.drawable.bg_item_128,
        R.drawable.bg_item_256,
        R.drawable.bg_item_512,
        R.drawable.bg_item_1024,
        R.drawable.bg_item_2048,
        R.drawable.bg_item_4096
    )
}

fun background(_amount: Int): Int {
    var degree = 0
    var amount = _amount

    while (amount > 0) {
        degree++
        amount /= 2
    }
    return BackgroundUtils.bgList[degree]
}