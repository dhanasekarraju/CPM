package com.chiller.pump;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.core.BasicRule;

public class TripStatusRule extends BasicRule {
    @Condition
    public boolean isTripStatusTrue(@Fact("statusNode") ObjectNode statusNode) {
        return statusNode.get("trip_status").asBoolean();
    }
}
