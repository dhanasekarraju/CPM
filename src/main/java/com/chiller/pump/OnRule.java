package com.chiller.pump;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.core.BasicRule;

public class OnRule extends BasicRule {
    @Condition
    public boolean isPumpOff(@Fact("statusNode") ObjectNode statusNode) {
        return !statusNode.get("on").asBoolean();
    }
}

