package local.ts3snet.unicbot_ms_spring.module_telegram_bots.model.torg_messages.impl;

import local.ts3snet.unicbot_ms_spring.module_telegram_bots.model.torg_messages.UnicBotTORGMessageAbstract;
import local.ts3snet.unicbot_ms_spring.module_telegram_bots.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Lazy
@Component
public class Help extends UnicBotTORGMessageAbstract {
    @Override
    public void execute(TelegramBotService bot, String... param) {
        bot.sendMessage(this.getUserId(), convertToUTF8(
                        "ну давай"
                                + "\n /sub подписаться на уведомления от бота"
                                + "\n /unsub отписаться"
                                + "\n /getkeys показать сохраненные ключи")
        );

        log.debug("-> help");
    }

    @Override
    public String messageType() {
        return "/help";
    }
}

