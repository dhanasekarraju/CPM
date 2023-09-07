package com.chiller.valve;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "OpenSecondaryValveRule", description = "Open the valve when a condition is met.")
public class OpenSecondaryValveRule {

    @Condition
    public boolean when(@Fact("syNode") ObjectNode syNode) {
        boolean valveStatus = syNode.get("sy_valve_on").asBoolean();
        boolean pumpOn = syNode.get("sy_on").asBoolean();
        boolean flow = syNode.get("sy_flow_on").asBoolean();

        if (pumpOn && flow) {
            if (valveStatus) {
                syNode.put("sy_valve_status", "valve open");
            } else {
                syNode.put("sy_valve_status", "Valve is closed so issued command to open");
                syNode.put("sy_valve_on", true);
            }
        } else {
            syNode.put("sy_valve_status", "Valve closed as Secondary Flow Switch and Pump is Off");
        }
        return valveStatus;
    }

    @Action
    public void then() {
    }
}

