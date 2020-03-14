package me.stqlth.solent.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.solent.messages.getMethods.GetMessageInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class StaffMessages {
    private GetMessageInfo getMessageInfo;

    public StaffMessages(GetMessageInfo getMessageInfo) {
        this.getMessageInfo = getMessageInfo;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Punishment Command Messages










    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setPrefix(TextChannel channel, String prefix) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Successfully changed the bot prefix to `" + prefix + "`!");
        try {
			channel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
    }
    public void prefixTooLarge(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Your prefix can only be 100 characters long.");
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }
    public void sendErrorMessagePrefix(TextChannel channel, Member member, CommandEvent event, String command) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        Guild g = event.getGuild();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Invalid Usage!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("Proper usage: " + getMessageInfo.getPrefix(g) + command + " <prefix>")
                .appendDescription("\n<> = Required, [] = Optional")
                .setFooter("© 2020 KryptoBot", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

}
