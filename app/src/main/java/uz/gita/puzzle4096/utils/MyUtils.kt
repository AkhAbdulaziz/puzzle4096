package uz.gita.puzzle4096.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import timber.log.Timber

fun timber(message: String, tag: String = "TTT") {
    Timber.tag(tag).d(message)
}

fun Activity.showToast(message: String, duration : Int= Toast.LENGTH_SHORT) {
    Toast.makeText(this,message, duration).show()
}

fun Fragment.showToast(message: String, duration : Int= Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(),message, duration).show()
}

fun List<Int>.show() : String {
    val sb = StringBuilder()
    for (i in this.indices)
        sb.append("${this[i]}" ,)
    return sb.toString()
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)