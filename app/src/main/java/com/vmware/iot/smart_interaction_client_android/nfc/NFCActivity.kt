package com.vmware.iot.smart_interaction_client_android.nfc

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.FormatException
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.vmware.iot.smart_interaction_client_android.network.controller.RetrofitController
import com.vmware.iot.smart_interaction_client_android.network.model.nfcReq.NfcScanReq
import com.vmware.iot.smart_interaction_client_android.network.model.nfcResp.NfcScanResp
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Created by IoT-Engg team on 31/05/18.
 */
class NFCActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val TYPE = "type"
    private val PAYLOAD = "payload"
    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this@NFCActivity,
                    "NFC not supported",
                    Toast.LENGTH_LONG).show()
        } else if(!nfcAdapter?.isEnabled!!) {
            Toast.makeText(this@NFCActivity,
                    "Please enable NFC first",
                    Toast.LENGTH_LONG).show()
        }
        
    }

    override fun onResume() {
        super.onResume()
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val nfcIntentFilter = arrayOf(techDetected, tagDetected, ndefDetected)

        val pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        if (nfcAdapter != null)
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null)
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null)
            nfcAdapter?.disableForegroundDispatch(this)
    }

    fun startNfcReader(context: Context) {
        context.startActivity(Intent(context, NFCActivity::class.java))
    }

    fun checkPermissions() {
        val myVersion = Build.VERSION.SDK_INT
        if (myVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission()
            }
        }
    }

    private fun checkIfAlreadyHavePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET), 101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //granted
            } else {
                //not granted
                finish()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    public fun setNfcListener(nfcListener: NfcListener) {
        if(null != Utils.listenersList) {
            Utils.listenersList.add(nfcListener)
        }
    }

    public fun removeNfcListener(nfcListener: NfcListener) {
        if(null != Utils.listenersList && Utils.listenersList.contains(nfcListener)) {
            Utils.listenersList.remove(nfcListener)
        }
    }

    override fun onNewIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

        Log.d(TAG, "onNewIntent: " + intent.action!!)

        var uid = bytesToHexString(tag!!.id)

        uid = getMacID(uid)
        Log.d(TAG, "uid: $uid")

        if(null != Utils.listenersList) {
            for(nfcListener in Utils.listenersList) {
                nfcListener.nfcTagRead(uid)
            }
        } else {
            Log.d(TAG, "No listener found")
        }

        try {
            if (tag != null) {
                val ndef = Ndef.get(tag)
                callNfcDetailsRequest(uid, readFromNFC(ndef))
            }
        } catch (t: Throwable) {
            Log.e(TAG, "Error--> ${t.printStackTrace()}")
        }
    }

    private fun callNfcDetailsRequest(nfcTagId: String, ndefData: String) {
        val nfcScanReq = NfcScanReq(nfcTagId, ndefData)

        val call = RetrofitController.instance.IoTDetails.getNfcTagDetails(nfcScanReq)
        Log.e(TAG, "getNfcTagDetails url--> ${call.request().url()}")
        call.enqueue(object : Callback<NfcScanResp> {
            override fun onResponse(call: Call<NfcScanResp>, response: Response<NfcScanResp>) {
                if(null != response && response.isSuccessful) {
                    Log.e(TAG, "onResponse--> ${response.body()}")
                    val resp = response.body() as NfcScanResp
                    if(null != Utils.listenersList) {
                        for(nfcListener in Utils.listenersList) {
                            nfcListener.nfcTagDetailsSuccess(resp.actionResponseMap.openUrlAction.response.url)
                        }
                    } else {
                        Log.d(TAG, "No listener found")
                    }
                } else {
                    if(null != Utils.listenersList) {
                        for(nfcListener in Utils.listenersList) {
                            nfcListener.nfcTagDetailsFailure("Response Unsuccessful")
                        }
                    } else {
                        Log.d(TAG, "No listener found")
                    }
                }
                this@NFCActivity.finish()
            }

            override fun onFailure(call: Call<NfcScanResp>, t: Throwable) {
                if(null != Utils.listenersList) {
                    for(nfcListener in Utils.listenersList) {
                        nfcListener.nfcTagDetailsFailure("Response Failed ${t.message}")
                    }
                } else {
                    Log.d(TAG, "No listener found")
                }
                this@NFCActivity.finish()
            }
        })
    }


    private fun getMacID(string: String): String {
        var string = string
        var macId = ""

        if (string.length % 2 == 0) {
            var i = 0
            while (string.isNotEmpty()) {
                if (i > 0) {
                    macId += ":"
                }
                macId += string.substring(0, 2)
                string = string.substring(2)
                i++
            }
        }
        return macId
    }

    private fun readFromNFC(ndef: Ndef): String {
        try {
            ndef.connect()
            val jsonArray = JSONArray()
            for(ndefMsg in ndef.ndefMessage.records) {
                val jsonObject = JSONObject()
                jsonObject.put(TYPE, hexToAscii(bytesToHexString(ndefMsg.type)))
                jsonObject.put(PAYLOAD, hexToAscii(bytesToHexString(ndefMsg.payload)))
                jsonArray.put(jsonObject)
            }

            Log.d(TAG, "TestTAG readFromNFC jsonArray: $jsonArray")
            return jsonArray.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return "Hi, I am NFC tag"
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        var st: String = ""
        for (b in bytes) {
            st += String.format("%02X", b)
        }
        return st
    }

    private fun hexToAscii(hexStr: String): String {
        val output = StringBuilder("")

        var i = 0
        while (i < hexStr.length) {
            val str = hexStr.substring(i, i + 2)
            output.append(Integer.parseInt(str, 16).toChar())
            i += 2
        }

        return output.toString()
    }
}
