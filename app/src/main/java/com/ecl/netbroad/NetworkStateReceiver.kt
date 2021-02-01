
package com.ecl.netbroad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import java.lang.Exception
import java.util.ArrayList


class NetworkStateReceiver(context: Context) : BroadcastReceiver() {

private var mManager: ConnectivityManager =
context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
private var mListeners: MutableList<NetworkStateReceiverListener> = ArrayList()
private var mConnected: Boolean = false

override fun onReceive(context: Context, intent: Intent?) {
try {
if (intent == null || intent.extras == null) return

if (checkStateChanged()) notifyStateToAll()
} catch (ex: Exception) {
ex.message
}
}

private fun checkStateChanged(): Boolean {

val prev = mConnected
try {
val activeNetwork = mManager.activeNetworkInfo
mConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
} catch (ex: Exception) {
ex.message
}

return prev != mConnected
}

private fun notifyStateToAll() {
try {
for (listener in mListeners) {
notifyState(listener)
}
} catch (ex: Exception) {
ex.message
}
}

private fun notifyState(listener: NetworkStateReceiverListener?) {

try {
if (listener != null) {
if (mConnected) listener.onNetworkAvailable()
else listener.onNetworkUnavailable()
}
} catch (ex: Exception) {
ex.message
}
}

//call this method to add a listener
fun addListener(l: NetworkStateReceiverListener) {
try {
mListeners.add(l)
notifyState(l)
} catch (ex: Exception) {
ex.message
}
}

//call this method to remove a listener
fun removeListener(l: NetworkStateReceiverListener) {
mListeners.remove(l)
}

interface NetworkStateReceiverListener {
fun onNetworkAvailable()

fun onNetworkUnavailable()
}

init {
try {
val intentFilter = IntentFilter()
intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
context.registerReceiver(this, intentFilter)
checkStateChanged()
} catch (ex: Exception) {
ex.message
}
}

}