package de.fw.teger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fw.teger.model.Location;
import de.fw.teger.model.Temperature;
import de.fw.teger.model.TemperatureMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
public class MqttMessageConverter extends DefaultPahoMessageConverter {


    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converts the MQTT message payload which is a JSON string to a @See TemperatureMessage
     * @param mqttMessage the mqtt-message with temperature-data
     * @return a TemperatureMessage
     * @throws Exception if conversion fails
     */
    @Override
    protected Object mqttBytesToPayload(MqttMessage mqttMessage) throws Exception {
        String messageString =  (String) super.mqttBytesToPayload(mqttMessage);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(messageString);
        Location location = new Location(json.get("location").get("name").asText());
        Temperature temperature = new Temperature(BigDecimal.valueOf(json.get("temperature").get("value").asDouble()));
        LocalDateTime dateTime = LocalDateTime.parse(json.get("time").asText(), dateTimeFormatter);
        return new TemperatureMessage(location,temperature,dateTime);
    }
}
