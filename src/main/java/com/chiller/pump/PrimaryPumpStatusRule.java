package com.chiller.pump;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

@Rule(name = "PrimaryPumpStatusRule", description = "Check pump status and set status message, run status, and time warning")
public class PrimaryPumpStatusRule {
    private static final Logger logger = LoggerFactory.getLogger(PrimaryPumpStatusRule.class);

    @Condition
    public boolean isPumpStatusValid(@Fact("pyNode") ObjectNode pyNode, @Fact("runtimeDuration") Duration runtimeDuration) {
        boolean on = pyNode.get("py_on").asBoolean();
        boolean tripStatusTrue = pyNode.get("py_trip_status").asBoolean();
        boolean valve = pyNode.get("py_valve_on").asBoolean();
        String mode = pyNode.get("py_mode").asText();
        long hours = runtimeDuration.toHours();
        double temp = pyNode.get("py_temperature").asDouble();
        double fTemp = pyNode.get("py_fixedTemp").asDouble();

        if (!tripStatusTrue && !"manual".equals(mode)) {
            pyNode.put("py_on", true);
            pyNode.put("py_run_status", "running");
            pyNode.put("py_status_message", "Pump started successfully");
            if (hours >= 24) {
                pyNode.put("py_on", false);
                pyNode.put("py_status_message", "Pump Stopped due to high Runtime");
                pyNode.put("py_time_warning", "Runtime exceeds 24 hours");
                pyNode.put("py_run_status", "not running");
            } else {
                pyNode.put("py_time_warning", "Runtime not exceeds 24 hours");
            }
            if (temp >= fTemp) {
                pyNode.put("py_on", false);
                pyNode.put("py_run_status", "not running");
                pyNode.put("py_status_message", "temperature is high");
            }
            return true;
        } else {
            pyNode.put("py_on", false);
            pyNode.put("py_run_status", "not running");
            if (tripStatusTrue){
                pyNode.put("py_status_message", "Cannot run py_pump due to trip");
            }else {
                pyNode.put("py_status_message", "Cannot run py_pump due to manual mode");
            }
            pyNode.put("py_time_warning", "Runtime not exceeds 24 hours");
            return false;
        }
    }

    @Action
    public void updatePumpStatus(@Fact("pyNode") ObjectNode pyNode) {
    }
}