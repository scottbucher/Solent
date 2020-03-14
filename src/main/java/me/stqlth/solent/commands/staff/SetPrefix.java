package me.stqlth.solent.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.solent.messages.discordOut.StaffMessages;
import me.stqlth.solent.utils.DatabaseMethods;
import me.stqlth.solent.utils.Utilities;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;

public class SetPrefix extends Command {

    private DatabaseMethods db;
    private StaffMessages staffMessages;

    public SetPrefix(DatabaseMethods databaseMethods, StaffMessages staffMessages) {
        this.name = "setprefix";
        this.help = "Sets the prefix of the bot. Default prefix is '!'";
        this.guildOnly = true;
        this.hidden = true;
        this.db = databaseMethods;
        this.staffMessages = staffMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();

        String[] args = event.getMessage().getContentRaw().split(" ");
        Guild g = event.getGuild();
        Member sender = event.getMember();
        Permission req = Permission.ADMINISTRATOR;

        if (!sender.hasPermission(req)) {
            Utilities.sendEmbed(channel, Color.RED, "Only admins may use this command!");
            return;
        }

        if (args.length != 2) {
            try {
                event.getMessage().delete().queue();
            } catch (InsufficientPermissionException ignored) {}
            staffMessages.sendErrorMessagePrefix(channel, sender, event, getName());
            return;
        }

        if (args[1].length() > 100) {
            Utilities.sendEmbed(channel, Color.RED, "That prefix is too large!");
            return;
        }

        if (db.updatePrefix(g, args[1])) {
            staffMessages.setPrefix(channel, args[1]);
            return;
        }
        Utilities.sendEmbed(channel, Color.RED, "Something went wrong. Try again later.");
    }
}
