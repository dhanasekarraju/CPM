package com.chiller.pump;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Rule(name = "CondenserPumpStatusRule", description = "Check pump status and set status message, run status, and time warning")
public class CondenserPumpStatusRule {
    private static final Logger logger = LoggerFactory.getLogger(CondenserPumpStatusRule.class);

    @Condition
    public boolean isPumpStatusValid(@Fact("pyNode") ObjectNode pyNode, @Fact("syNode") ObjectNode syNode, @Fact("cdNode") ObjectNode cdNode, @Fact("runtimeDuration") Duration runtimeDuration) {
        boolean on = cdNode.get("cd_on").asBoolean();
        boolean tripStatusTrue = cdNode.get("cd_trip_status").asBoolean();
        boolean valve = cdNode.get("cd_valve_on").asBoolean();
        String mode = cdNode.get("cd_mode").asText();
        long hours = runtimeDuration.toHours();
        double temp = cdNode.get("cd_temperature").asDouble();
        double fTemp = cdNode.get("cd_fixedTemp").asDouble();

        if (!tripStatusTrue && !"manual".equals(mode) && valve) {
            cdNode.put("cd_on", true);
            cdNode.put("cd_run_status", "running");
            cdNode.put("cd_status_message", "Pump started successfully");
            if (hours >= 24) {
                cdNode.put("cd_on", false);
                cdNode.put("cd_status_message", "Pump Stopped due to high Runtime");
                cdNode.put("cd_time_warning", "Runtime exceeds 24 hours");
                cdNode.put("cd_run_status", "not running");
            } else {
                cdNode.put("cd_time_warning", "Runtime not exceeds 24 hours");
            }
            if (temp >= fTemp) {
                cdNode.put("cd_on", false);
                cdNode.put("cd_run_status", "not running");
                cdNode.put("cd_status_message", "temperature is high");
            }
            return true;
        } else {
            cdNode.put("cd_on", false);
            pyNode.put("py_on", false);
            pyNode.put("py_valve_on", false);
            pyNode.put("py_valve_status", "Valve stopped because condenser pump is not working");
            pyNode.put("py_status_message", "Pump stopped because condenser pump is not working");
            syNode.put("sy_on", false);
            syNode.put("sy_valve_on", false);
            syNode.put("sy_valve_status", "Valve stopped because condenser pump is not working");
            syNode.put("sy_status_message", "Pump stopped because condenser pump is not working");
            cdNode.put("cd_run_status", "not running");
            if (tripStatusTrue){
                cdNode.put("cd_status_message", "Cannot run cd_pump due to trip");
            }else {
                cdNode.put("cd_status_message", "Cannot run cd_pump due to manual mode");
            }
            cdNode.put("cd_time_warning", "Runtime not exceeds 24 hours");
            return false;
        }
    }

    @Action
    public void updatePumpStatus(@Fact("cdNode") ObjectNode cdNode) {
    }
}