package com.chiller.valve;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "OpenPrimaryValveRule", description = "Open the valve when a condition is met.")
public class OpenPrimaryValveRule {

    @Condition
    public boolean when(@Fact("pyNode") ObjectNode pyNode) {
        boolean valveStatus = pyNode.get("py_valve_on").asBoolean();

        if (valveStatus){
            pyNode.put("py_valve_status", "valve open");
        }else {
            pyNode.put("py_valve_status", "Valve is closed so issued command to open");
            pyNode.put("py_valve_on", true);
        }
        return valveStatus;
    }

    @Action
    public void then() {
    }
}

