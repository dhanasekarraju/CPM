package com.chiller.master;

import com.chiller.chillerunit.ChillerOnRule;
import com.chiller.flowswitch.CondenserFlowSwitchRule;
import com.chiller.flowswitch.PrimaryFlowSwitchRule;
import com.chiller.flowswitch.SecondaryFlowSwitchRule;
import com.chiller.pump.CondenserPumpStatusRule;
import com.chiller.pump.PrimaryPumpStatusRule;
import com.chiller.pump.SecondaryPumpStatusRule;
import com.chiller.valve.OpenCondenserValveRule;
import com.chiller.valve.OpenCoolingTowerValveRule;
import com.chiller.valve.OpenPrimaryValveRule;
import com.chiller.valve.OpenSecondaryValveRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;

@RestController
@RequestMapping("/admin")
public class MasterOperator {
    private static final Logger logger = LoggerFactory.getLogger(MasterOperator.class);

    @GetMapping("/master")
    public String status() {
        String outputJson = null;
        Instant startTime = Instant.parse("2023-09-04T18:00:00Z");
        Instant stopTime = Instant.parse("2023-09-05T17:00:00Z");

        Duration runtimeDuration = Duration.between(startTime, stopTime);

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("device_id", "chiller_unit_001");
        outputNode.put("timestamp", System.currentTimeMillis());

        ObjectNode pyNode = objectMapper.createObjectNode();
        pyNode.put("py_valve_on", false);
        pyNode.put("py_valve_status", "");
        pyNode.put("py_mode", "auto");
        pyNode.put("py_trip_status", false);
        pyNode.put("py_on", false);
        pyNode.put("py_temperature", "24");
        pyNode.put("py_fixedTemp", "25");
        pyNode.put("py_start", "2023-09-04T18:00:00Z");
        pyNode.put("py_stop", "2023-09-05T17:00:00Z");
        pyNode.put("py_run_status", "");
        pyNode.put("py_status_message", "");
        pyNode.put("py_flow_on", true);
        pyNode.put("py_flow_status", "");

        ObjectNode syNode = objectMapper.createObjectNode();
        syNode.put("sy_mode", "auto");
        syNode.put("sy_trip_status", false);
        syNode.put("sy_on", false);
        syNode.put("sy_temperature", "24");
        syNode.put("sy_fixedTemp", "25");
        syNode.put("sy_start", "2023-09-04T18:00:00Z");
        syNode.put("sy_stop", "2023-09-05T17:00:00Z");
        syNode.put("sy_run_status", "");
        syNode.put("sy_status_message", "");
        syNode.put("sy_flow_on", true);
        syNode.put("sy_flow_status", "");
        syNode.put("sy_valve_on", false);
        syNode.put("sy_valve_status", "");

        ObjectNode cdNode = objectMapper.createObjectNode();
        cdNode.put("cd_valve_on", false);
        cdNode.put("cd_valve_status", "");
        cdNode.put("cd_mode", "auto");
        cdNode.put("cd_trip_status", true);
        cdNode.put("cd_on", false);
        cdNode.put("cd_temperature", "24");
        cdNode.put("cd_fixedTemp", "25");
        cdNode.put("cd_start", "2023-09-04T18:00:00Z");
        cdNode.put("cd_stop", "2023-09-05T17:00:00Z");
        cdNode.put("cd_run_status", "");
        cdNode.put("cd_status_message", "");
        cdNode.put("cd_flow_on", true);
        cdNode.put("cd_flow_status", "");

        ObjectNode ctNode = objectMapper.createObjectNode();
        ctNode.put("ct_temperature", 24.0);
        ctNode.put("ct_fixed_temp", 25.0);
        ctNode.put("ct_valve_on", false);
        ctNode.put("ct_valve_status", "");

        ObjectNode chillNode = objectMapper.createObjectNode();
        chillNode.put("ch_temperature", 24.0);
        chillNode.put("ch_fixed_temp", 25.0);
        chillNode.put("ch_on", true);
        chillNode.put("ch_status", "");

        ObjectNode masterNode = objectMapper.createObjectNode();
        masterNode.set("py_pump", pyNode);
        masterNode.set("sy_pump", syNode);
        masterNode.set("cd_pump", cdNode);
        masterNode.set("ct_pump", ctNode);
        masterNode.set("chiller", chillNode);

        final Rules rules = new Rules();
        rules.register(new OpenPrimaryValveRule());
        final Rules rules1 = new Rules();
        rules1.register(new PrimaryPumpStatusRule());
        final Rules rules2 = new Rules();
        rules2.register(new PrimaryFlowSwitchRule());
        final Rules rules3 = new Rules();
        rules3.register(new SecondaryPumpStatusRule());
        final Rules rules4 = new Rules();
        rules4.register(new SecondaryFlowSwitchRule());
        final Rules rules5 = new Rules();
        rules5.register(new OpenSecondaryValveRule());
        final Rules rules6 = new Rules();
        rules6.register(new OpenCondenserValveRule());
        final Rules rules7 = new Rules();
        rules7.register(new CondenserPumpStatusRule());
        final Rules rules8 = new Rules();
        rules8.register(new CondenserFlowSwitchRule());
        final Rules rules9 = new Rules();
        rules9.register(new OpenCoolingTowerValveRule());
        final Rules rules10 = new Rules();
        rules10.register(new ChillerOnRule());

        Facts facts = new Facts();
        RulesEngine rulesEngine = new DefaultRulesEngine();
        facts.put("pyNode", pyNode);
        facts.put("syNode", syNode);
        facts.put("cdNode", cdNode);
        facts.put("ctNode", ctNode);
        facts.put("chillNode", chillNode);
        facts.put("runtimeDuration", runtimeDuration);

        rulesEngine.fire(rules, facts);
        rulesEngine.fire(rules1, facts);
        rulesEngine.fire(rules2, facts);
        rulesEngine.fire(rules3, facts);
        rulesEngine.fire(rules4, facts);
        rulesEngine.fire(rules5, facts);
        rulesEngine.fire(rules6, facts);
        rulesEngine.fire(rules7, facts);
        rulesEngine.fire(rules8, facts);
        rulesEngine.fire(rules9, facts);
        rulesEngine.fire(rules10, facts);

        outputNode.set("master_status", masterNode);

        try {
            outputJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(outputNode);
        } catch (JsonProcessingException e) {
            logger.error("JSON failed to write");
        }

        System.out.println(outputJson);

        return outputJson;
    }
}