package com.example.wordsearch.device.utility

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wordsearch.ui.view.dialog.DialogOK
import com.google.android.material.snackbar.Snackbar
import java.text.*
import java.util.*

fun TextView.setCurrencyText(price: Double) {
    val indianLocale = Locale("en", "IN")
    val nf = NumberFormat.getCurrencyInstance(indianLocale)
    nf.minimumFractionDigits = 0
    nf.maximumFractionDigits = 0
    this.text = nf.format(price)
}

fun getCurrencyText(price: Double): String {
    val indianLocale = Locale("en", "IN")
    val nf = NumberFormat.getCurrencyInstance(indianLocale)
    nf.minimumFractionDigits = 0
    nf.maximumFractionDigits = 0
    return nf.format(price)
}

fun getCurrencyText(price: Float): String {
    val indianLocale = Locale("en", "IN")
    val nf = NumberFormat.getCurrencyInstance(indianLocale)
    nf.minimumFractionDigits = 0
    nf.maximumFractionDigits = 0
    return nf.format(price)
}

fun Context.showToastInCenter(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).also {
        it.setGravity(Gravity.CENTER, 0, 0)
    }.show()
}

fun getDecimalValue(doubleValue: Double): String {
    return String.format(Locale.US, "%.2f", doubleValue)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invis() {
    visibility = View.INVISIBLE
}

fun EditText.getString() = text.toString()
fun EditText.getDouble() = text.toString().toDouble()
fun EditText.getInt() = text.toString().toInt()

fun TextView.getString() = text.toString()

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}


fun AppCompatActivity.showKeyboardForEditText(editText: EditText) {
    // Delay showing the keyboard to give the view some time to set up
    editText.postDelayed({
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        editText.requestFocus()
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }, 200)
}


fun EditText.onTextChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = cb(s.toString())
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun String?.toTwoChar(): String {

    return when {
        isNullOrEmpty() -> "?"
        contains(" ") -> replace("^\\s*([a-zA-Z]).*\\s+([a-zA-Z])\\S+$".toRegex(), "$1$2").uppercase()
        length > 1 -> substring(0, 2)
        else -> substring(0, 1)
    }

}

fun toTitleCase(string: String?): String? {
    // Check if String is null
    if (string == null) {
        return null
    }
    var whiteSpace = true
    val builder = StringBuilder(string) // String builder to store string
    val builderLength = builder.length
    // Loop through builder
    for (i in 0 until builderLength) {
        val c = builder[i] // Get character at builders position
        if (whiteSpace) {
            // Check if character is not white space
            if (!Character.isWhitespace(c)) {

                // Convert to title case and leave whitespace mode.
                builder.setCharAt(i, Character.toTitleCase(c))
                whiteSpace = false
            }
        } else if (Character.isWhitespace(c)) {
            whiteSpace = true // Set character is white space
        } else {
            builder.setCharAt(i, Character.toLowerCase(c)) // Set character to lowercase
        }
    }
    return builder.toString() // Return builders text
}

fun Double.toDoubleFormat(): String {
    val pattern = "#,###.00"
    val decimalFormat = DecimalFormat(pattern)
    decimalFormat.groupingSize = 3
    return decimalFormat.format(this)
}

fun Double.toPrice(): String {
    val pattern = "#,###.00"
    val decimalFormat = DecimalFormat(pattern)
    decimalFormat.groupingSize = 3
    return "₹" + decimalFormat.format(this)
}

val String.containsDigit: Boolean
    get() = matches(Regex(".*[0-9].*"))

val String.isAlphanumeric: Boolean
    get() = matches(Regex("[a-zA-Z0-9]*"))

val String.asUri: Uri?
    get() = try {
        if (URLUtil.isValidUrl(this)) Uri.parse(this) else null
    } catch (e: Exception) {
        null
    }

fun Any?.isNull() = this == null
fun Any?.isNotNull() = this != null

fun snackBar(view: View, text: String) = Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    return simpleDateFormat.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun getStartDate(): String {
    var startDate = "01-04-"
    val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    try {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val presentDate = simpleDateFormat.format(calendar.time)
        val date: Date? = inputFormat.parse(presentDate)
        if (date != null) {
            calendar.time = date
        }
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        startDate += if (month > 2) {
            year
        } else {
            year - 1
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return startDate
}

@SuppressLint("SimpleDateFormat")
fun getEndDate(): String {
    var endDate = "31-03-"
    val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    try {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val presentDate = simpleDateFormat.format(calendar.time)
        val date: Date? = inputFormat.parse(presentDate)
        if (date != null) {
            calendar.time = date
        }
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        endDate += if (month > 2) {
            year + 1
        } else {
            year
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return endDate
}

@SuppressLint("SimpleDateFormat")
fun formatDateShort(sourceDate: String?): String? {
    val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    val outputFormat: DateFormat = SimpleDateFormat("yyMMdd")
    var date: Date? = null
    try {
        date = sourceDate?.let { inputFormat.parse(it) }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date?.let { outputFormat.format(it) }
}

@SuppressLint("SimpleDateFormat")
fun formatDate(sourceDate: String?): String? {
    val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    val outputFormat: DateFormat = SimpleDateFormat("dd MMM, yyyy")
    var date: Date? = null
    try {
        date = sourceDate?.let { inputFormat.parse(it) }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date?.let { outputFormat.format(it) }
}

fun getGender(): ArrayList<String> {
    return object : ArrayList<String>() {
        init {
            add("male")
            add("female")
        }
    }
}

fun AppCompatActivity.dialogOk(alert: String, message: String, callBack: DialogOK.IDialogOKCallback) {
    val dialog = DialogOK.newInstance(alert, message, callBack)
    val ft = supportFragmentManager.beginTransaction()
    ft.add(dialog, null)
    ft.commitAllowingStateLoss()
}

