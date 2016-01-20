package de.fw.teger;

import de.fw.teger.model.TemperatureMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@IntegrationComponentScan
public class MessageConfig {

    @Value("${mqttServerUrl}")
    private String mqttServerUrl;

    @Autowired
    private MqttMessageConverter mqttMessageConverter;


    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel officeStream() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public MessageChannel outsideStream() {
        return new PublishSubscribeChannel();
    }


    @Bean
    public MessageChannel temperatureStream() {
        return new PublishSubscribeChannel();
    }


    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttServerUrl, "teger", "temp-stream");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(mqttMessageConverter);
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Router(inputChannel = "mqttInputChannel")
    public List<MessageChannel> route(Message<TemperatureMessage> message) {
        List<MessageChannel> channels = new ArrayList<>();
        channels.add(temperatureStream());
        if ("office".equals(message.getPayload().getLocation().getName())) {
            channels.add(officeStream());
        }
        if ("outside".equals(message.getPayload().getLocation().getName())) {
            channels.add(outsideStream());
        }
        return channels;
    }
}
