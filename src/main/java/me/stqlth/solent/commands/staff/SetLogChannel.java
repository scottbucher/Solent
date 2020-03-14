package me.stqlth.solent.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.solent.utils.DatabaseMethods;
import me.stqlth.solent.utils.Utilities;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class SetLogChannel extends Command {

	private DatabaseMethods db;

	public SetLogChannel(DatabaseMethods databaseMethods) {
        this.name = "setLogs";
        this.help = "Set the log message channel";
		this.arguments = "[#channel]";
        this.guildOnly = true;
        this.hidden = true;

		this.db = databaseMethods;
	}


	@Override
	protected void execute(CommandEvent event) {
		TextChannel channel = event.getTextChannel();

		Member sender = event.getMember();
		Permission req = Permission.ADMINISTRATOR;

		if (!sender.hasPermission(req)) {
			Utilities.sendEmbed(channel, Color.RED, "Only admins may use this command!");
			return;
		}

		String[] args = event.getMessage().getContentRaw().split(" ");

		if (args.length > 2) return;

		TextChannel logChannel;

		try {
			logChannel = event.getMessage().getMentionedChannels().get(0);
		} catch (IndexOutOfBoundsException e) {
			if (args.length == 1)
				logChannel = event.getTextChannel();
			else {
				Utilities.sendEmbed(channel, Color.RED, "That channel cannot be found!");
				return;
			}
		}

		db.updateLogChannel(event, logChannel);
		Utilities.sendEmbed(channel, Color.GREEN, "Successfully set the log channel to **" + logChannel.getAsMention() + "**!");
	}
}
