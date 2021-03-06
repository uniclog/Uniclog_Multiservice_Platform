package local.ts3snet.unicbot_ms_spring.module_teamspeak.service.impl.utils.impl;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import local.ts3snet.unicbot_ms_spring.module_teamspeak.model.CommandLet;
import local.ts3snet.unicbot_ms_spring.module_teamspeak.model.uniccore_messages.TeamspeakMessageAbstract;
import local.ts3snet.unicbot_ms_spring.module_teamspeak.model.uniccore_messages.impl.Default;
import local.ts3snet.unicbot_ms_spring.module_teamspeak.service.impl.utils.TeamspeakEventAdapter;
import local.ts3snet.unicbot_ms_spring.module_teamspeak.service.impl.utils.TeamspeakMessageSender;
import local.ts3snet.unicbot_ms_spring.module_telegram.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TeamspeakEventAdapterImpl extends TS3EventAdapter implements TeamspeakEventAdapter {
    private int botId;
    private TS3Api api;
    private final Map<Integer, ClientInfo> clients = new HashMap<>();

    private final TelegramBotService telegramUnicBotCoreUserService;
    private final TeamspeakMessageSender messageSender;
    private final Map<String, TeamspeakMessageAbstract> messages;
    private final CommandLet commandLet;
    @Autowired
    public TeamspeakEventAdapterImpl(
            @Qualifier(value = "unicBotCoreTelegramBotServiceImpl") TelegramBotService telegramBotService,
            TeamspeakMessageSender messageSender,
            @Qualifier("ts3sMessageMap")
            Map<String, TeamspeakMessageAbstract> messages,
            @Qualifier("ts3sMessageCommandLet")
            CommandLet commandLet) {
        this.telegramUnicBotCoreUserService = telegramBotService;
        this.messageSender = messageSender;
        this.messages = messages;
        this.commandLet = commandLet;
    }

    @Override
    public void setApi(TS3Api api) {
        this.api = api;
        this.botId = api.whoAmI().getId();
        this.api.getClients()
                .forEach(c -> clients.put(c.getId(), api.getClientInfo(c.getId())));

        String info = clients
                .entrySet()
                .stream()
                .map(c -> c.getKey() + " - " + c.getValue().getNickname())
                .collect(Collectors.joining(", "));
        log.info("Clients on server: " + info);
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        api.sendPrivateMessage(e.getClientId(),
                " welcome to the party! " +
                        "\n Commands: " +
                        "\n !use loginnotify" +
                        "\n !mytime" +
                        "\n !wakeup");
        // send to telegram
        telegramUnicBotCoreUserService.sendMessageForAllSubscribers(null,e.getClientNickname() + " joined server");
        ClientInfo client = api.getClientInfo(e.getClientId());
        log.info(client.getNickname() + " joined server");

        clients.put(e.getClientId(), client);
    }
    @Override
    public void onClientLeave(ClientLeaveEvent e) {
        ClientInfo clientInfo = clients.get(e.getClientId());
        telegramUnicBotCoreUserService.sendMessageForAllSubscribers(null, clientInfo.getNickname() + " left server");
        log.info(clientInfo.getNickname() + " left server");

        clients.remove(e.getClientId());
    }
    @Override
    public void onTextMessage(TextMessageEvent e) {
        if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != botId) {
            // Default msg = new Default()
            // msg.setTextMessage("Hello " + e.getInvokerName() + "!")
            // messageSender.sendChannelMessage(null, msg)


            String message = e.getMessage();
            log.info("Received: " + message);
            String authorSignature = e.getInvokerName();
            Integer userId = e.getInvokerId();
            String invokerUniqueId = e.getInvokerUniqueId();

            commandLet.update(message);
            TeamspeakMessageAbstract msg =
                    messages.getOrDefault(commandLet.getCmd().getTextValue(), new Default());
            msg.setTextMessage(message);
            msg.setUserId(userId);
            msg.setUserToken(invokerUniqueId);
            msg.setUserName(authorSignature);

            msg.execute(messageSender, commandLet.getFirstParameter(), commandLet.getAllMessage());
        }
    }
}
