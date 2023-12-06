package com.wanjohi.david.demoapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.*
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.max

object Config {
    var isNetworkConnected: Boolean = false
    var isUnMeteredNetworkConnected: Boolean = false
    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}

fun isUrlInList(url: String, list: Array<String>): Boolean {
    return (list.any { url.startsWith(it, true) })
}


fun checkPermissions(context: Context, permissions: Array<String>): Array<String> {
    return permissions.filter { permission ->
        ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
    }.toTypedArray()
}


fun Date.getAgeSimpleString(): String {
    val now = Date()
    val years = now.getYearsSince(this)
    val months = now.getMonthsSince(this)
    val days = now.getDaysSince(this)

    val monthsInY = now.getMonthsOfYearSince(this)
    val daysInM = now.getDaysOfMonthSince(this)

    if (years > 0) return "${years}y" + if (years < 5 && monthsInY > 0) " ${monthsInY}m" else ""
    if (months > 0) return "${months}m" + if (months < 10 && daysInM > 0) " ${daysInM}d" else ""
    if (days > 0) return "${days}d"
    return "0"
}

fun Date.getYearsSince(last: Date): Int {
    val a = getCalendar(this)
    val b = getCalendar(last)
    var years = floor(
        ((12 * a.get(Calendar.YEAR) + a.get(Calendar.MONTH)) -
                (12 * b.get(Calendar.YEAR) + b.get(Calendar.MONTH))) / 12.0
    ).toInt()
    if (a.get(Calendar.MONTH) == b.get(Calendar.MONTH)) {
        if (a.get(Calendar.DAY_OF_MONTH) < b.get(Calendar.DAY_OF_MONTH)) {
            years -= 1
        }
    }
    return years
}

fun Date.getMonthsSince(last: Date): Int {
    val a = getCalendar(this)
    val b = getCalendar(last)
    var months = (12*a.get(Calendar.YEAR)+a.get(Calendar.MONTH)) -
            (12*b.get(Calendar.YEAR)+b.get(Calendar.MONTH))
    if (a.get(Calendar.MONTH) == b.get(Calendar.MONTH)) {
        if (a.get(Calendar.DAY_OF_MONTH) < b.get(Calendar.DAY_OF_MONTH)) {
            months -= 1
        }
    }
    return months
}

fun Date.getMonthsOfYearSince(last: Date): Int {
    val a = getCalendar(this)
    val b = getCalendar(last)
    var months = a.get(Calendar.MONTH) - b.get(Calendar.MONTH)
    if (a.get(Calendar.MONTH) == b.get(Calendar.MONTH)) {
        if (a.get(Calendar.DAY_OF_MONTH) < b.get(Calendar.DAY_OF_MONTH)) {
            months -= 1
        }
    }
    return max(0, months)
}

fun Date.add(field: Int, amount: Int): Date {
    val a = getCalendar(this)
    a.add(field, amount)
    return a.time
}

fun Date.getRelativeDateStr(
    format: SimpleDateFormat,
    dateDiffStringMap: Map<Int, String>? = null,
): String {
    val date = getCalendar(this)
    val today = getCalendar(Date())
    val map = dateDiffStringMap ?: mapOf(
            Pair(-1, "Yesterday"),
            Pair(0, "Today"),
            Pair(1, "Tomorrow"),
        )
    val dateStr = format.format(this)
    if (date.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
        val diff = date.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR)
        return map.getOrElse(diff, { dateStr })
    }
    return dateStr
}

// TODO not very accurate
fun Date.getDaysSince(last: Date): Int {
    val a = getCalendar(this)
    val b = getCalendar(last)
    return (365*a.get(Calendar.YEAR)+a.get(Calendar.DAY_OF_YEAR))+ -
            (365*b.get(Calendar.YEAR)+b.get(Calendar.DAY_OF_YEAR))
}

// TODO not very accurate
fun Date.getDaysOfMonthSince(last: Date): Int {
    val a = getCalendar(this)
    val b = getCalendar(last)
    val days = a.get(Calendar.DAY_OF_MONTH) - b.get(Calendar.DAY_OF_MONTH)
    return max(0, days)
}

fun getCalendar(date: Date): Calendar {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    cal.time = date
    return cal
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}

fun <T> LiveData<T>.observeUntilSuccess(isSuccessful: (T?) -> Boolean) {
    observeForever(object : Observer<T> {
        override fun onChanged(value: T) {
            try {
                if (isSuccessful(value)) removeObserver(this)
            } catch (e: Exception) {
                removeObserver(this)
                throw e
            }
        }
    })
}

fun resolveOrThrow(
    context: Context,
    @AttrRes attributeResId: Int
): Int {
    val typedValue = TypedValue()
    if (context.theme.resolveAttribute(attributeResId, typedValue, true)) {
        return typedValue.data
    }
    throw IllegalArgumentException(context.resources.getResourceName(attributeResId))
}

fun getDensity(context: Context): Float {
    return context.resources.displayMetrics.density
}

fun getPixel(context: Context, dp: Float): Int {
    return (context.resources.displayMetrics.density * dp + 0.5).toInt()
}

fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

fun String.toTitleCase(): String {
    val input = this
    val titleCase = StringBuilder(input.length)
    var nextTitleCase = true
    var char: Char
    for (c in input.toCharArray()) {
        char = c
        if (Character.isSpaceChar(char)) {
            nextTitleCase = true
        } else if (nextTitleCase) {
            char = Character.toTitleCase(char)
            nextTitleCase = false
        }
        titleCase.append(char)
    }
    return titleCase.toString()
}

fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.notifyAllItemChanged() {
    for (i in 0 until this.itemCount) {
        this.notifyItemChanged(i)
    }
}

fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>
        .notifyItemChangedIf(predicate: (T) -> Boolean) {
    for (i in 0 until this.itemCount) {
        val item = currentList.getOrNull(i) ?: continue
        if (predicate(item))
            this.notifyItemChanged(i)
    }
}



@SuppressLint("Range")
fun getImagePath(context: Context, uri: Uri?, selection: String?): String {
    var path: String? = null
    val cursor = context.contentResolver?.query(uri!!, null, selection, null, null )
    if (cursor != null){
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        }
        cursor.close()
    }
    return path!!
}
object FileUtil {
    fun from(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val file = createTempFile(context)
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }

    private fun createTempFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }
}