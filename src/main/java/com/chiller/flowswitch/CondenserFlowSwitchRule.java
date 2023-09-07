package com.chiller.flowswitch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "CondenserFlowSwitchRule", description = "Flow switch should be on to confirm water flow.")
public class CondenserFlowSwitchRule {
    private boolean flowStatus;

    @Condition
    public boolean when(@Fact("cdNode") ObjectNode cdNode) {
        boolean pumpOn = cdNode.get("cd_on").asBoolean();
        this.flowStatus = cdNode.get("cd_flow_on").asBoolean();
        boolean trip = cdNode.get("cd_trip_status").asBoolean();
        String mode = cdNode.get("cd_mode").asText();

        if (this.flowStatus && pumpOn) {
            cdNode.put("cd_flow_status", "Water Flow is normal");
            cdNode.put("cd_run_status", "running");
            cdNode.put("cd_status_message", "Pump is running");
        } else {
            cdNode.put("cd_flow_on", false);
            cdNode.put("cd_flow_status", "Water Flow not detected so Switching off pump and valve");
            cdNode.put("cd_on", false);
            cdNode.put("cd_run_status", "not running");
            cdNode.put("cd_valve_on", false);
            cdNode.put("cd_valve_status", "Valve closed due to no water flow");
            if (trip) {
                cdNode.put("cd_status_message", "Pump stopped due trip");
            } else if ("manual".equals(mode)) {
                cdNode.put("cd_status_message", "Pump stopped due to manual mode");
            } else {
                cdNode.put("cd_status_message", "Pump stopped due to no water flow");
            }
        }
        return true;
    }

    @Action
    public void then() {

    }

    public void setFlowStatus(boolean flowStatus) {
        this.flowStatus = flowStatus;
    }
}

