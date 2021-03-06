package local.ts3snet.unicbot_ms_spring.module_telegram.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Component
@Scope("prototype")
public class CommandLet {
    private String cmd = MessageType.DEFAULT;
    private String firstParameter = MessageType.DEFAULT;
    private String allMessage = MessageType.DEFAULT;

    public String getCmd() {
        return cmd.toLowerCase(Locale.ROOT);
    }

    /**
     * Find /command pattern in line
     * @param msg source message
     */
    public void update(String msg) {
        String regex = "(?<cmd>/\\w+)(\\s+)(?<value>\\w+)";
        Matcher matcher = Pattern.compile(regex).matcher(msg);
        if (matcher.find()) {
            this.setCmd( matcher.group("cmd") );
            this.setFirstParameter( matcher.group("value") );
        } else this.setCmd( MessageType.DEFAULT );

        regex = "(?<cmd>/\\w+)(\\s+)(?<value>.*)";
        matcher = Pattern.compile(regex).matcher(msg);
        this.setAllMessage(matcher.find() ? matcher.group("value") : MessageType.DEFAULT);

        if (this.getCmd().equals(MessageType.DEFAULT)) {
            regex = "(?<cmd>/\\w+)";
            matcher = Pattern.compile(regex).matcher(msg);
            this.setCmd(matcher.find() ? matcher.group("cmd") : MessageType.DEFAULT);
        }

    }
}
