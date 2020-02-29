import music.MusicHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class CommandHandler {
    private final String userName;
    private final String userMessage;
    private final MessageReceivedEvent event;

    public CommandHandler() {
        this.userName = MessageHandler.userName;
        this.userMessage = MessageHandler.userMessageText;
        this.event = MessageHandler.event;

        handleCmd();
    }

    public void handleCmd() {
        if (userMessage.length() <= 1) {
            return;
        }
        String[] args = userMessage.split("\\s+", 2);
        String command = args[0].substring(1);
        switch (command) {
            case "join":
                new VoiceHandler().join();
                break;
            case "leave":
                new VoiceHandler().leave();
                break;
            case "play":
                new VoiceHandler().play(args[1]);
                break;
            case "skip":
                new VoiceHandler().skip();
                break;
            case "upvote":
                MessageHandler.sendMessage("Aha, ok das war der Upvote-Befehl");
                break;
            case "cat":
                sendCatImage(event.getChannel());
                break;
        }
    }

    public void sendCatImage(MessageChannel channel) {
        String url = "https://cataas.com/cat";
        EmbedBuilder result = new EmbedBuilder();
        result.setTitle("Hier ist deine Gatze!");
        result.setColor(Color.red);

        OkHttpClient http = event.getJDA().getHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = http.newCall(request).execute();
            InputStream body = response.body().byteStream();
            result.setImage("attachment://image.png");
            Response finalResponse = response;

            channel.sendMessage(result.build())
                    .addFile(body, "image.png")
                    .queue(m -> finalResponse.close(), error -> {
                        finalResponse.close();
                        RestAction.getDefaultFailure().accept(error);
                    });
        } catch (IOException e) {
            e.printStackTrace();
            response.close();
        }
    }
}