package com.chiller.flowswitch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "PrimaryFlowSwitchRule", description = "Flow switch should be on to confirm water flow.")
public class PrimaryFlowSwitchRule {
    private boolean flowStatus;

    @Condition
    public boolean when(@Fact("pyNode") ObjectNode pyNode) {
        boolean pumpOnOff = pyNode.get("py_on").asBoolean();
        this.flowStatus = pyNode.get("py_flow_on").asBoolean();
        boolean trip = pyNode.get("py_trip_status").asBoolean();
        String mode = pyNode.get("py_mode").asText();

        if (this.flowStatus && pumpOnOff) {
            pyNode.put("py_flow_status", "Water Flow is normal");
            pyNode.put("py_run_status", "running");
            pyNode.put("py_status_message", "Pump is running");
            return true;
        } else {
            pyNode.put("py_flow_on", false);
            pyNode.put("py_flow_status", "Water Flow not detected so Switching off pump and valve");
            pyNode.put("py_run_status", "not running");
            pyNode.put("py_valve_on", false);
            if (!pumpOnOff){
                pyNode.put("py_valve_status", "Valve closed as pump is off");
            } else {
                pyNode.put("py_valve_status", "Valve closed due to no water flow");
            }
            if (trip) {
                pyNode.put("py_status_message", "Pump stopped due to trip");
                return true;
            } else if ("manual".equals(mode)) {
                pyNode.put("py_status_message", "Pump stopped due to manual mode");
                return true;
            } else {
                pyNode.put("py_status_message", "Pump stopped due to no water flow");
                return true;
            }
        }
    }

    @Action
    public void then() {

    }

    public void setFlowStatus(boolean flowStatus) {
        this.flowStatus = flowStatus;
    }
}

