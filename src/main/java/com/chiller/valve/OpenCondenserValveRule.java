package com.chiller.valve;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "OpenCondenserValveRule", description = "Open the valve when a condition is met.")
public class OpenCondenserValveRule {

    @Condition
    public boolean when(@Fact("syNode") ObjectNode syNode, @Fact("cdNode") ObjectNode cdNode) {
        boolean syValveStatus = syNode.get("sy_valve_on").asBoolean();
        boolean cdValveStatus = cdNode.get("cd_valve_on").asBoolean();
        if (syValveStatus) {
            if (cdValveStatus) {
                cdNode.put("cd_valve_status", "valve open");
            } else {
                cdNode.put("cd_valve_status", "Valve is closed so issued command to open");
                cdNode.put("cd_valve_on", true);
            }
        } else {
            cdNode.put("cd_valve_status", "Valve is closed as secondary valve is not open");
        }
        return true;
    }

    @Action
    public void then() {
    }
}

