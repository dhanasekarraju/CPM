package com.chiller.valve;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "OpenCondenserValveRule", description = "Open the valve when a condition is met.")
public class OpenCoolingTowerValveRule {

    @Condition
    public boolean when(@Fact("ctNode") ObjectNode ctNode, @Fact("cdNode") ObjectNode cdNode) {
        boolean valveStatus = ctNode.get("ct_valve_on").asBoolean();
        boolean pumpOn = cdNode.get("cd_on").asBoolean();
        boolean flow = cdNode.get("cd_flow_on").asBoolean();

        if (pumpOn && flow) {
            if (valveStatus) {
                ctNode.put("ct_valve_status", "valve open");
            } else {
                ctNode.put("ct_valve_status", "Valve is closed so issued command to open");
                ctNode.put("ct_valve_on", true);
            }
        } else {
            ctNode.put("ct_valve_status", "Valve closed as Condenser Flow Switch and Pump is Off");
        }
        return valveStatus;
    }

    @Action
    public void then() {
    }
}

