
package com.vmware.iot.smart_interaction_client_android.network.model.nfcResp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "success",
    "message",
    "actionResponseMap"
})
public class NfcScanResp {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("actionResponseMap")
    private ActionResponseMap actionResponseMap;

    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("actionResponseMap")
    public ActionResponseMap getActionResponseMap() {
        return actionResponseMap;
    }

    @JsonProperty("actionResponseMap")
    public void setActionResponseMap(ActionResponseMap actionResponseMap) {
        this.actionResponseMap = actionResponseMap;
    }

}
