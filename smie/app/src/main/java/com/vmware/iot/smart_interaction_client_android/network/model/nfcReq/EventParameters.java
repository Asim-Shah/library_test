
package com.vmware.iot.smart_interaction_client_android.network.model.nfcReq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nfcTagId",
    "ndefData"
})
public class EventParameters {

    @JsonProperty("nfcTagId")
    private String nfcTagId;
    @JsonProperty("ndefData")
    private String ndefData;

    public EventParameters(String nfcTagId, String ndefData) {
        setNfcTagId(nfcTagId);
        setNdefData(ndefData);
    }

    @JsonProperty("nfcTagId")
    public String getNfcTagId() {
        return nfcTagId;
    }

    @JsonProperty("nfcTagId")
    public void setNfcTagId(String nfcTagId) {
        this.nfcTagId = nfcTagId;
    }

    @JsonProperty("ndefData")
    public String getNdefData() {
        return ndefData;
    }

    @JsonProperty("ndefData")
    public void setNdefData(String ndefData) {
        this.ndefData = ndefData;
    }

}
