import music.MusicHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public final class VoiceHandler {
    private final MessageReceivedEvent event = MessageHandler.event;
    private final MessageChannel channel = event.getChannel();
    private final AudioManager audioManager = event.getGuild().getAudioManager();
    private final VoiceChannel connectedChannel = event.getMember().getVoiceState().getChannel();
    private final VoiceChannel botsConnectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();

    public void join() {
        if (connectedChannel == null) {
            MessageHandler.sendMessage("Ey, du bist ja nirgendwo drinnen");
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission((event.getGuild().getGuildChannelById(connectedChannel.getId())), Permission.VOICE_CONNECT)) {
            MessageHandler.sendMessage("Nein, ADMIN hat gesagt ich darf das nicht");
            return;
        }

        if (audioManager.isAttemptingToConnect()) {
            MessageHandler.sendMessage("JA BIN GLEICH DA... komm mal runter ey");
            return;
        }
        MessageHandler.sendMessage("**Bruder, bin gleich da!**");
        audioManager.openAudioConnection(connectedChannel);
    }

    public void leave() {
        if (botsConnectedChannel == null) {
            MessageHandler.sendMessage("Ey, ich bin ja nirgendwo drinnen");
            MessageHandler.sendMessageWithDelay("Frechheit", 3, TimeUnit.SECONDS, true);
            return;
        }
        MessageHandler.sendMessage("**Bruder, muss los**");
        audioManager.closeAudioConnection();
    }

    public void play(String identifier) {
        if (botsConnectedChannel == null) {
            join();
        }

        if (!isValidURL(identifier)) {
            try {
                String keyword = identifier;
                keyword = keyword.replace(" ", "+");

                String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&q=" + keyword + "&key=AIzaSyBtTogZxUjllmJNoEhaxHaUa_iCVLqIzi4";

                Document doc = Jsoup.connect(url).timeout(10 * 1000).ignoreContentType(true).get();
                String getJson = doc.text();
                JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();

                String videoId = (String) ((JSONObject) ((JSONObject) jsonObject.getJSONArray("items").get(0)).get("id")).get("videoId");
                identifier = "https://www.youtube.com/watch?v=" + videoId;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new MusicHandler().loadAndPlay((TextChannel) channel, identifier);
    }

    public void skip() {
        if (connectedChannel == null) {
            MessageHandler.sendMessage("Ey, ich mach ja nicht mal was");
            return;
        }
        new MusicHandler().skipTrack((TextChannel) (channel));
    }

    private static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}



