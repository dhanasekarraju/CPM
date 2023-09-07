package com.chiller.pump;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class RunningStatusRule implements Rule {
    private final ObjectNode statusNode;

    public RunningStatusRule(ObjectNode statusNode) {
        this.statusNode = statusNode;
    }

    @Override
    public boolean evaluate(Facts facts) {
        boolean on = statusNode.get("on").asBoolean();
        boolean notAutoMode = !"auto".equals(statusNode.get("mode").asText());
        boolean tripStatusTrue = statusNode.get("trip_status").asBoolean();

        if (!on || notAutoMode || tripStatusTrue) {
            statusNode.put("run_status", "not running");
        } else {
            statusNode.put("run_status", "running");
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


