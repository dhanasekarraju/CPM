package com.chiller.pump;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.core.BasicRule;

public class ModeRule extends BasicRule {
    @Condition
    public boolean isNotAutoMode(@Fact("statusNode") ObjectNode statusNode) {
        return !"auto".equals(statusNode.get("mode").asText());
    }
}
