package local.ts3snet.unicbot_ms_spring.module_teamspeak_utils.service.impl;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import local.ts3snet.unicbot_ms_spring.module_teamspeak_utils.config.TeamspeakBotConfig;
import local.ts3snet.unicbot_ms_spring.module_teamspeak_utils.service.TeamspeakBotService;
import local.ts3snet.unicbot_ms_spring.module_teamspeak_utils.service.utils.TeamspeakUtils;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TeamspeakBotServiceImpl implements TeamspeakBotService {
    private String address;
    private String password;
    private String login;

    private final TeamspeakUtils teamspeakUtils;
    private final TeamspeakBotConfig config;

    public TeamspeakBotServiceImpl(TeamspeakBotConfig config, TeamspeakUtils teamspeakUtils) {
        this.config = config;
        this.teamspeakUtils = teamspeakUtils;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        this.address = config.getIpAddress();
        this.login = config.getLogin();
        this.password = config.getPassword();

        this.register();
    }

    @Override
    public void register() {
        final TS3Config cfg = new TS3Config();
        cfg.setHost(address);
        cfg.setEnableCommunicationsLogging(true);
        cfg.setReconnectStrategy(ReconnectStrategy.constantBackoff());
        cfg.setConnectionHandler(new ConnectionHandler() {
            @Override
            public void onConnect(TS3Api api) {
                stuffThatNeedsToRunEveryTimeTheQueryConnects(api);
            }
            @Override
            public void onDisconnect(TS3Query ts3Query) {
                // Nothing
            }
        });
        final TS3Query query = new TS3Query(cfg);
        query.connect();

        stuffThatOnlyEverNeedsToBeRunOnce(query.getApi());
        query.exit();
    }

    private void stuffThatNeedsToRunEveryTimeTheQueryConnects(TS3Api api) {
        api.login(login, password);
        api.selectVirtualServerById(1);
        api.setNickname(login);
        int clientId = api.whoAmI().getId();
        api.moveClient(clientId, 32);
        api.registerAllEvents();
        // may be this case TS3EventType.TEXT_CHANNEL
    }

    private void stuffThatOnlyEverNeedsToBeRunOnce(TS3Api api) {
        // We only want to greet people once
        api.sendChannelMessage(" is online!");

        // On the API side of things, you only need to register your TS3Listeners once!
        // These are not affected when the query disconnects.
        teamspeakUtils.setApi(api);
        api.addTS3Listeners(teamspeakUtils);
    }

}