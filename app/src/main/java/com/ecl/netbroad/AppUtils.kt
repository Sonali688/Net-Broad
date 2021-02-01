package com.ecl.netbroad

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView

import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object AppUtils {

    fun isValidEmailId(email: String): Boolean {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
            .matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val passwordPattern = "^" + "(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                // "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$"
        pattern = Pattern.compile(passwordPattern)
        matcher = pattern.matcher(password)

        return matcher.matches()
    }

    fun disableNavigationViewScrollbars(navigationView: NavigationView?) {
        if (navigationView != null) {
            val navigationMenuView = navigationView.getChildAt(0) as NavigationMenuView
            navigationMenuView.isVerticalScrollBarEnabled = false
        }
    }

    fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun showToast(context: Context, message: String?) {
        if (message.isNullOrEmpty()) {
            val msg = context.getString(R.string.something_went_wrong)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }



    fun hideKeyboard(context: Context) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = (context as Activity).currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun getPref(key: String, context: Context): String? {

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, null)
    }

    fun capitalizeSingle(s: String?): String? {
        if (s == null) return null
        if (s.length == 1) {
            return s.toUpperCase()
        }
        return if (s.length > 1) {
            s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase()

        } else ""
    }

    fun filterValue(value: String?): String {
        return value?.replace("\n", "")?.replace("null", "")?.replace("  ", " ") ?: ""
    }


    fun putPrefBoolean(key: String, value: Boolean, context: Context?) {
        if (context == null) return
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun deleteSharedData(context: Context?) {
        if (context == null) return
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun putPref(key: String, value: String, context: Context?) {
        if (context == null) return
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun deletePrefKey(key: String, context: Context) {
        val sPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getPrefBoolean(key: String, context: Context): Boolean {

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(key, false)
    }

    fun goToMainActivity(ctx: Context) {
        val intent = Intent(ctx, MainActivity::class.java)
        ctx.startActivity(intent)
        (ctx as AppCompatActivity).finish()
    }



    fun getTomorrowDate(): String? {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(tomorrow)
    }

    fun mdatePickerDialog(context: Context, text: TextView) {

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newCalender = Calendar.getInstance()
        newCalender.timeZone = TimeZone.getTimeZone("UTC")
        val datepicker = DatePickerDialog(
            context,
            { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                text.text = simpleDateFormat.format(newDate.time)
            },
            newCalender.get(Calendar.YEAR),
            newCalender.get(Calendar.MONTH),
            newCalender.get(Calendar.DAY_OF_MONTH)
        )
        //        datepicker.datePicker.maxDate = System.currentTimeMillis()
        datepicker.show()
    }

    fun datePickerDialog(context: Context, text: TextView) {

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newCalender = Calendar.getInstance()
        newCalender.timeZone = TimeZone.getTimeZone("UTC")
        val datepicker = DatePickerDialog(
            context,
            { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                text.text = simpleDateFormat.format(newDate.time)
            },
            newCalender.get(Calendar.YEAR),
            newCalender.get(Calendar.MONTH),
            newCalender.get(Calendar.DAY_OF_MONTH)
        )
        datepicker.show()
    }

    fun futureDatePickerDialog(context: Context, text: TextView) {

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newCalender = Calendar.getInstance()
        newCalender.timeZone = TimeZone.getTimeZone("UTC")
        val datepicker = DatePickerDialog(
            context,
            { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                text.text = simpleDateFormat.format(newDate.time)
            },
            newCalender.get(Calendar.YEAR),
            newCalender.get(Calendar.MONTH),
            newCalender.get(Calendar.DAY_OF_MONTH)
        )
        datepicker.datePicker.minDate = System.currentTimeMillis() + 43200000
        datepicker.show()
    }

    fun pastDatePickerDialog(context: Context, text: TextView) {

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newCalender = Calendar.getInstance()
        newCalender.timeZone = TimeZone.getTimeZone("UTC")
        val datepicker = DatePickerDialog(
            context,
            { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                text.text = simpleDateFormat.format(newDate.time)
            },
            newCalender.get(Calendar.YEAR),
            newCalender.get(Calendar.MONTH),
            newCalender.get(Calendar.DAY_OF_MONTH)
        )

        newCalender.add(Calendar.MONTH, -1)
        val days = newCalender.getActualMaximum(Calendar.DAY_OF_MONTH)

        val mCurrentDay = (getCurrentDate().take(2).toLong() + days) - 1
        val total_day = mCurrentDay.times(86400000)
        datepicker.datePicker.minDate = System.currentTimeMillis() - total_day
        datepicker.show()
    }

    fun timePickerDialog(context: Context, text: TextView) {

        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val second = mcurrentTime.get(Calendar.SECOND)
        val miliSecond = mcurrentTime.get(Calendar.MILLISECOND)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
            if (second < 10) {
                val mTime = "$selectedHour:$selectedMinute:0$second"
                text.text = mTime
            } else {
                val mTime = "$selectedHour:$selectedMinute:$second"
                text.text = mTime
            }

        }, hour, minute, false)
        mTimePicker.show()
    }



    fun startCall(ctx: Context, value: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$value")
            ctx.startActivity(intent)
        } catch (e: Exception) {
            showToast(ctx, e.message)
        }
    }


    fun sendToMail(ctx: Context, value: String) {
        val emailArray: Array<String> = arrayOf(value)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, emailArray)
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        if (intent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(intent)
        }
    }

    fun openGoogleMap(ctx: Context, latitude: String, longitude: String) {
        val uri = String.format(Locale.ENGLISH, "google.navigation:q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        ctx.startActivity(intent)
    }


    fun getCurrentDate(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    fun parseDate(value: String?): String {
        val dateFormat1 = SimpleDateFormat("dd MMM yyyy")
        val dateFormat2 = SimpleDateFormat("dd/MM/yyyy")
        val date = dateFormat1.parse(value)
        return dateFormat2.format(date)
    }

    fun getLastDate(): String {
        val today: LocalDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val endDate = today.withDayOfMonth(today.lengthOfMonth())
        val targetFormat = SimpleDateFormat("dd/MM/yyyy")
        val mDate = originalFormat.parse("$endDate")
        return targetFormat.format(mDate)
    }


    fun removeZero(value: String): String {
        val mValue = value.toDouble()
        val format = DecimalFormat("0.#")
        return format.format(mValue)
    }

    fun getAddress(context: Context, lat: String, long: String): String {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val mLat = lat.toDoubleOrNull()
            val mLong = long.toDoubleOrNull()
            if (mLat != null && mLong != null) {
                val addresses: List<Address> = geocoder.getFromLocation(mLat, mLong, 1)
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return strAdd
    }

    fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = "v ${pInfo.versionName}"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }



    fun getCityName(context: Context, mLat: Double?, mLong: Double?): String? {
        var mCity: String? = ""
        try {
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addresses = mLat?.let { mLong?.let { it1 -> geoCoder.getFromLocation(it, it1, 1) } }
            mCity = addresses?.get(0)?.locality
        } catch (e: Exception) {
            e.message
        }
        return mCity
    }

    fun getCurrentYear(): Int {
        val cal = Calendar.getInstance()
        return cal[Calendar.YEAR]
    }

    fun getCurrentMonth(): Int {
        val cal = Calendar.getInstance()
        return cal[Calendar.MONTH]
    }

    fun setLastMargin(view: View) {
        val param = view.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(20, 20, 20, 20)
        view.layoutParams = param
    }

    fun getDay(date: String): String {
        val format1 = SimpleDateFormat("dd/MM/yyyy")
        val dt = format1.parse(date)
        val format2: DateFormat = SimpleDateFormat("EEEE")
        return format2.format(dt).take(3)
    }

    fun changeDateFormat(strDate: String): String? {
        val f: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy").toFormatter()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val parsedDate = LocalDate.parse(strDate, f)
        val f2: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        return parsedDate.format(f2)
    }

    fun getFirstDate(): String {
        return "01" + getCurrentDate().takeLast(8)
    }

    fun extractYoutubeId(url: String?): String? {
        val query: String = URL(url).query
        val param = query.split("&".toRegex()).toTypedArray()
        var id: String? = null
        for (row in param) {
            val param1 = row.split("=".toRegex()).toTypedArray()
            if (param1[0] == "v") {
                id = param1[1]
            }
        }
        return id
    }




    fun getAddress(context: Context, mLat: Double?, mLong: Double?): String {

        var strAdd = ""
        val geoCoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = mLat?.let { mLong?.let { it1 -> geoCoder.getFromLocation(it, it1, 1) } }
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = java.lang.StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }

                strAdd = strReturnedAddress.toString()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return strAdd
    }


    fun getMonthValue(): Int {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[Calendar.MONTH]
    }


    fun capitalize(msg: String?): String {

        val str = msg ?: ""
        str.toLowerCase()
        val s = StringBuffer()
        var ch = ' '

        for (i in str.indices) {
            if (ch == ' ' && str[i] != ' ') s.append(Character.toUpperCase(str[i])) else s.append(
                str[i]
            )
            ch = str[i]
        }

        return s.toString().trim { it <= ' ' }
    }

}