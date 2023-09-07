package com.chiller.chillerunit;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "OnChiller", description = "On the chiller when a condition is met.")
public class ChillerOnRule {

    @Condition
    public boolean when(@Fact("cdNode") ObjectNode cdNode, @Fact("chillNode") ObjectNode chillNode) {
        boolean flowOn = cdNode.get("cd_flow_on").asBoolean();

        if (flowOn){
            chillNode.put("ch_on", true);
            chillNode.put("ch_status", "Chiller Started successfully");
        }else {
            chillNode.put("ch_on", false);
            chillNode.put("ch_status", "Chiller not started due to no condenser water flow");
        }
        return true;
    }

    @Action
    public void then() {
    }
}

