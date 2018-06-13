
package com.vmware.iot.smart_interaction_client_android.network.model.nfcResp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "OpenUrlAction"
})
public class ActionResponseMap {

    @JsonProperty("OpenUrlAction")
    private OpenUrlAction openUrlAction;

    @JsonProperty("OpenUrlAction")
    public OpenUrlAction getOpenUrlAction() {
        return openUrlAction;
    }

    @JsonProperty("OpenUrlAction")
    public void setOpenUrlAction(OpenUrlAction openUrlAction) {
        this.openUrlAction = openUrlAction;
    }

}
