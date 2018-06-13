package com.vmware.iot.smart_interaction_client_android.nfc

import java.io.Serializable

/**
 * Created by IoT-Engg team on 31/05/18.
 */
interface NfcListener : Serializable {
    public fun nfcTagRead(tagId: String)
    public fun nfcTagDetailsSuccess(url: String)
    public fun nfcTagDetailsFailure(error: String)
}