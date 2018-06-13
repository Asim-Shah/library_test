package com.vmware.iot.smart_interaction_client_android.network

import com.vmware.iot.smart_interaction_client_android.network.model.nfcReq.NfcScanReq
import com.vmware.iot.smart_interaction_client_android.network.model.nfcResp.NfcScanResp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by IoT-Engg team on 01/06/18.
 */
interface IoTApi {
    @POST(NFC_URL)
    fun getNfcTagDetails(@Body body: NfcScanReq): Call<NfcScanResp>

    companion object {
        const val OKHTTP_CLIENT_TIMEOUT = 60
        const val CONTENT_TYPE = "application/json"
        const val FAILURE = "FAILURE"
        const val PORT_NO = "3232"
        const val BASE_URL = "http://s2-dev-mobile-2:$PORT_NO"
        const val NFC_URL = "/smart-interaction-engine/api/v1/engine/events"
    }
}