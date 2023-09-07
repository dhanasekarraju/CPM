package com.chiller.flowswitch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "SecondaryFlowSwitchRule", description = "Flow switch should be on to confirm water flow.")
public class SecondaryFlowSwitchRule {
    private boolean flowStatus;

    @Condition
    public boolean when(@Fact("pyNode") ObjectNode pyNode, @Fact("syNode") ObjectNode syNode) {
        boolean pumpOnOff = syNode.get("sy_on").asBoolean();
        this.flowStatus = syNode.get("sy_flow_on").asBoolean();
        boolean trip = syNode.get("sy_trip_status").asBoolean();
        String mode = syNode.get("sy_mode").asText();
        boolean pyFlow = pyNode.get("py_flow_on").asBoolean();

        if (pumpOnOff) {
            if (this.flowStatus) {
                syNode.put("sy_flow_status", "Water Flow is normal");
                syNode.put("sy_run_status", "running");
                syNode.put("sy_status_message", "Pump is running");
                return true;
            } else {
                syNode.put("sy_flow_status", "Water Flow not detected so Switching off pump and valve");
                syNode.put("sy_on", false);
                syNode.put("sy_run_status", "not running");
                syNode.put("sy_valve_on", false);
//                syNode.put("sy_valve_status", "Valve closed due to no water flow");
                if (trip) {
                    syNode.put("sy_status_message", "Pump stopped due trip");
                    return true;
                } else if ("manual".equals(mode)) {
                    syNode.put("sy_status_message", "Pump stopped due to manual mode");
                    return true;
                } else if (!pyFlow) {
                    syNode.put("sy_status_message", "Pump stopped due to no Primary pump water Flow");
                    return true;
                } else {
                    syNode.put("sy_status_message", "Pump stopped due to no water flow");
                    return true;
                }
            }
        } else {
            syNode.put("sy_on", false);
            syNode.put("sy_flow_on", false);
            syNode.put("sy_run_status", "not running");
            syNode.put("sy_status_message", "Secondary Pump is Off");
            syNode.put("sy_flow_status", "Secondary Pump is Off");
            return true;
        }
    }

    @Action
    public void then() {

    }

    public void setFlowStatus(boolean flowStatus) {
        this.flowStatus = flowStatus;
    }
}

