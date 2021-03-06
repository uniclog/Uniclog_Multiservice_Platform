package local.ts3snet.unicbot_ms_spring.module_telegram.model.torg_messages.impl;

import local.ts3snet.unicbot_ms_spring.core.entity.TelegramTORGUserEntity;
import local.ts3snet.unicbot_ms_spring.module_telegram.model.torg_messages.UnicBotTORGMessageAbstract;
import local.ts3snet.unicbot_ms_spring.module_telegram.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Lazy
@Component
public class UnSub extends UnicBotTORGMessageAbstract {
    @Override
    public void execute(TelegramBotService bot, String... param) {
        TelegramTORGUserEntity user = new TelegramTORGUserEntity();
        user.setUserTelegramId(this.getUserId());
        user.setSubscriber(false);
        user.setUserName(this.getUserName());

        getTelegramTORGUserService().update(user);

        log.debug("-> unsub");
    }

    @Override
    public String messageType() {
        return "/unsub";
    }
}
