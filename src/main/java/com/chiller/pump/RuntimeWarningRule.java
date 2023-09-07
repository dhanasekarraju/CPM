package com.chiller.pump;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import java.time.Duration;

public class RuntimeWarningRule implements Rule {
    private final Duration runtimeDuration;
    private final ObjectNode StatusNode;

    public RuntimeWarningRule(Duration runtimeDuration, ObjectNode StatusNode) {
        this.runtimeDuration = runtimeDuration;
        this.StatusNode = StatusNode;
    }

    @Override
    public boolean evaluate(Facts facts) {
        long hours = runtimeDuration.toHours();

        if (hours >= 24) {
            this.StatusNode.put("run_status", "not running");
            this.StatusNode.put("runtime_warning", "Warning: Runtime exceeds 24 hours");
        } else {
            this.StatusNode.put("runtime_warning", "OK");
        }
        return true;
    }

    @Override
    public void execute(Facts facts) {
        // Rule actions, if needed
    }

    @Override
    public int compareTo(Rule rule) {
        return 0;
    }
}


