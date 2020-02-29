import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class MessageHandler extends ListenerAdapter {
    public static MessageReceivedEvent event;
    public static String userName;
    public static String userMessageText;

    public final String[] REACTION_ALPHABET = new String[]{
            "\uD83C\uDDE6", "\uD83C\uDDE7", "\uD83C\uDDE8", "\uD83C\uDDE9", "\uD83C\uDDEA",
            "\uD83C\uDDEB", "\uD83C\uDDEC", "\uD83C\uDDED", "\uD83C\uDDEE", "\uD83C\uDDEF",
            "\uD83C\uDDF0", "\uD83C\uDDF1", "\uD83C\uDDF2", "\uD83C\uDDF3", "\uD83C\uDDF4",
            "\uD83C\uDDF5", "\uD83C\uDDF6", "\uD83C\uDDF7", "\uD83C\uDDF8", "\uD83C\uDDF9",
            "\uD83C\uDDFA", "\uD83C\uDDFB", "\uD83C\uDDFC", "\uD83C\uDDFD", "\uD83C\uDDFE",
            "\uD83C\uDDFF",
    };


    public void onMessageReceived(MessageReceivedEvent event) {
        userName = event.getAuthor().getName();
        userMessageText = event.getMessage().getContentRaw();
        this.event = event;

        if (event.getAuthor().isBot()) {
            return;
        } else if (userMessageText.startsWith(">")) {
            new CommandHandler();
        } else {
            handleReactions();
        }

        System.out.println("Bruder, *" + userName + "* hat geschrieben");
        System.out.println(">   '" + userMessageText + "'");
    }

    private void handleReactions() {
        if (userMessageText.equalsIgnoreCase("mulm")) {
            sendMessage(getRandomMulm());
        }

        if (userMessageText.toLowerCase().contains("tupay")) {
            reactText("AHAOKSTRICH");
        }
    }

    public static void sendMessage(String msg, boolean withTyping) {
        if (withTyping) {
            event.getChannel().sendTyping().queue();
        }
        event.getChannel().sendMessage(msg).queue();
    }

    public static void sendMessage(String msg) {
        sendMessage(msg, false);
    }

    public static void sendMessageWithDelay(String msg, long delay, TimeUnit timeUnit, boolean withTyping) {
        if (withTyping) {
            event.getChannel().sendTyping().queueAfter(delay, timeUnit);
        }
        event.getChannel().sendMessage(msg).queueAfter(delay, timeUnit);
    }

    private String getRandomMulm() {
        String[] mulmPool = {"Mulm", "MULM", "Mlum", "Mulum", "Mülm", "M U L M", "MUULM!", "MUUUUUUUUUULMMM!!!"};
        int random = (int) (1 + Math.random() * (mulmPool.length - 1));
        return mulmPool[random];
    }

    public void reactText(String text) {
        text = text.toUpperCase();
        final String[] REPLACEMENTS = new String[]{"a2:675795426162638901", "\uD83C\uDD71️", "", "", "", "", "", "h2:675792789618950173", "", ""};
        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (text.substring(i + 1).contains(Character.toString(c)) && ALPHABET.indexOf(c) < REPLACEMENTS.length) {
                event.getMessage().addReaction(REPLACEMENTS[ALPHABET.indexOf(c)]).queue();
            } else {
                event.getMessage().addReaction(REACTION_ALPHABET[ALPHABET.indexOf(c)]).queue();
            }
        }
    }
}
