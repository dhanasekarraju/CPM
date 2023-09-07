package com.chiller.tripstatusOld;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TemperatureControlConverter {

    public static void main(String[] args) {
        String inputJson = "{\"device_id\":\"edge_device_001\",\"timestamp\":\"2023-09-04T10:30:00Z\",\"temperature\":{\"value\":26,\"unit\":\"Celsius\"},\"humidity\":{\"value\":50.2,\"unit\":\"Percentage\"},\"status\":\"normal\"}";

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode inputNode;

        {
            try {
                inputNode = objectMapper.readTree(inputJson);

                double fixedTemperatureLimit = 25.0;

                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("device_id", inputNode.get("device_id").asText());
                outputNode.put("timestamp", inputNode.get("timestamp").asText());

                outputNode.set("temperature", inputNode.get("temperature"));
                outputNode.set("humidity", inputNode.get("humidity"));

                if (inputNode.get("temperature").get("value").asDouble() > fixedTemperatureLimit) {
                    ObjectNode alertsNode = objectMapper.createObjectNode();
                    ((ObjectNode) alertsNode).put("temperature_alert", createAlertNode("Warning", "Temperature exceeds 25 degrees Celsius."));
                    outputNode.set("alerts", alertsNode);
                } else {
                    ObjectNode alertsNode = objectMapper.createObjectNode();
                    ((ObjectNode) alertsNode).put("temperature_alert", createAlertNode("OK", "Temperature within acceptable range."));
                    outputNode.set("alerts", alertsNode);
                }

                String outputJson = objectMapper.writeValueAsString(outputNode);

                System.out.println(outputJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static JsonNode createAlertNode(String status, String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode alertNode = objectMapper.createObjectNode();
        alertNode.put("status", status);
        alertNode.put("message", message);
        return alertNode;
    }
}
