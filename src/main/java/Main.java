import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjY5NjE2NjUyMDM5NjE4NTY5.Xlrd8w.jTmXObpX6oUYS6YBoQHyx-iNXYA";
        builder.setToken(token);
        builder.addEventListeners(new MessageHandler());

        builder.setActivity(Activity.listening("Hundsgemeiner"));

        builder.build();
    }

}
