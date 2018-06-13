
package com.vmware.iot.smart_interaction_client_android.network.model.nfcReq;

import android.os.Build;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "deviceId",
    "eventSourceId",
    "eventParameters",
    "eventName"
})
public class NfcScanReq {

    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("eventSourceId")
    private int eventSourceId;
    @JsonProperty("eventParameters")
    private EventParameters eventParameters;
    @JsonProperty("eventName")
    private String eventName;

    public NfcScanReq(String nfcTagId, String ndefData) {
        setDeviceId(Build.SERIAL);
        setEventSourceId(502);
        setEventParameters(new EventParameters(nfcTagId, ndefData));
        setEventName("NFCScannedEvent");
    }


    @JsonProperty("deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("deviceId")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("eventSourceId")
    public int getEventSourceId() {
        return eventSourceId;
    }

    @JsonProperty("eventSourceId")
    public void setEventSourceId(int eventSourceId) {
        this.eventSourceId = eventSourceId;
    }

    @JsonProperty("eventParameters")
    public EventParameters getEventParameters() {
        return eventParameters;
    }

    @JsonProperty("eventParameters")
    public void setEventParameters(EventParameters eventParameters) {
        this.eventParameters = eventParameters;
    }

    @JsonProperty("eventName")
    public String getEventName() {
        return eventName;
    }

    @JsonProperty("eventName")
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

}
