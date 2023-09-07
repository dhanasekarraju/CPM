package com.chiller.pump;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Rule(name = "SecondaryPumpStatusRule", description = "Check pump status and set status message, run status, and time warning")
public class SecondaryPumpStatusRule {
    private static final Logger logger = LoggerFactory.getLogger(SecondaryPumpStatusRule.class);

    @Condition
    public boolean isPumpStatusValid(@Fact("pyNode") ObjectNode pyNode, @Fact("syNode") ObjectNode syNode, @Fact("runtimeDuration") Duration runtimeDuration) {
        boolean tripStatusTrue = syNode.get("sy_trip_status").asBoolean();
        String mode = syNode.get("sy_mode").asText();
        long hours = runtimeDuration.toHours();
        double temp = syNode.get("sy_temperature").asDouble();
        double fTemp = syNode.get("sy_fixedTemp").asDouble();
        boolean pyFlowStatus = pyNode.get("py_flow_on").asBoolean();

        if (pyFlowStatus) {
            if (!tripStatusTrue && !"manual".equals(mode)) {
                syNode.put("sy_on", true);
                syNode.put("sy_run_status", "running");
                syNode.put("sy_status_message", "Pump started successfully");
                if (hours >= 24) {
                    syNode.put("sy_on", false);
                    syNode.put("sy_status_message", "Pump Stopped due to high Runtime");
                    syNode.put("sy_time_warning", "Runtime exceeds 24 hours");
                    syNode.put("sy_run_status", "not running");
                } else {
                    syNode.put("sy_time_warning", "Runtime not exceeds 24 hours");
                }
                if (temp >= fTemp) {
                    syNode.put("sy_on", false);
                    syNode.put("sy_run_status", "not running");
                    syNode.put("sy_status_message", "Temperature is high");
                }
                return true;
            } else {
                syNode.put("sy_on", false);
                pyNode.put("py_on", false);
                pyNode.put("py_valve_on", false);
                pyNode.put("py_valve_status", "Valve stopped because second pump is not working");
                pyNode.put("py_status_message", "Pump stopped because second pump is not working");
                syNode.put("sy_run_status", "not running");
                if (tripStatusTrue) {
                    syNode.put("sy_status_message", "Cannot run pump due to trip");
                } else {
                    syNode.put("sy_status_message", "Cannot run pump due to manual mode");
                }
                syNode.put("sy_time_warning", "Runtime not exceeds 24 hours");
                return false;
            }
        } else {
            syNode.put("sy_status_message", "Primary pump is not active");
            return false;
        }
    }

    @Action
    public void updatePumpStatus(@Fact("pyNode") ObjectNode pyNode) {
    }
}