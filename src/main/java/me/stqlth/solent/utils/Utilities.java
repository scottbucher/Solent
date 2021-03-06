package me.stqlth.solent.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class Utilities {

    public String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%01d:%02d:%02d", hours, minutes, seconds);
    }

    public static Color getAverageColor(String url) {
        if (url == null) {
            return new Color(27, 137, 255);
        }
        try {
            BufferedImage img = ImageIO.read(Unirest.get(url).asBinary().getRawBody());
            int x0 = 0;
            int y0 = 0;
            int x1 = x0 + img.getWidth();
            int y1 = y0 + img.getHeight();
            long sumr = 0, sumg = 0, sumb = 0;
            for (int x = x0; x < x1; x++) {
                for (int y = y0; y < y1; y++) {
                    Color pixel = new Color(img.getRGB(x, y));
                    sumr += pixel.getRed();
                    sumg += pixel.getGreen();
                    sumb += pixel.getBlue();
                }
            }
            int num = img.getWidth() * img.getHeight();
            return new Color((int) sumr / num, (int) sumg / num, (int) sumb / num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Color(27, 137, 255);
    }
    public static boolean isPrivate(CommandEvent event) {
        TextChannel textChannel = null;
        PrivateChannel privateChannel = null;
        try {
            textChannel = event.getTextChannel();
        } catch (IllegalStateException ignored) {
            privateChannel = event.getPrivateChannel();
        }
        boolean normal = true;

        return  (privateChannel != null);
    }
    public static void sendEmbed(TextChannel channel, Color color, String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(color)
                .setDescription(message);
        try {
            channel.sendMessage(builder.build()).queue();
        } catch (InsufficientPermissionException ignored) {}
    }

}
