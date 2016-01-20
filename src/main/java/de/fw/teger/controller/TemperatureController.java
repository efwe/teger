package de.fw.teger.controller;

import de.fw.teger.model.TemperatureMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))@Controller
public class TemperatureController {

    private final SimpMessagingTemplate template;

    @ServiceActivator(inputChannel = "officeStream")
    public void officeMessage(Message<TemperatureMessage> message) throws Exception {
        this.template.convertAndSend("/topic/officeStream", message.getPayload());
    }


    @ServiceActivator(inputChannel = "outsideStream")
    public void outsideMessage(Message<TemperatureMessage> message) throws Exception {
        this.template.convertAndSend("/topic/outsideStream", message.getPayload());
    }

}
