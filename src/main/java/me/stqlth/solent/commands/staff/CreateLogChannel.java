package me.stqlth.solent.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.solent.utils.DatabaseMethods;
import me.stqlth.solent.utils.Utilities;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.util.EnumSet;

public class CreateLogChannel extends Command {

	private DatabaseMethods db;

	public CreateLogChannel(DatabaseMethods databaseMethods) {
		this.name = "createLogs";
		this.help = "Creates the log channel";
		this.guildOnly = true;
		this.hidden = true;

		this.db = databaseMethods;
	}

	@Override
	protected void execute(CommandEvent event) {
		TextChannel channel = event.getTextChannel();

		Member sender = event.getMember();

		Permission req = Permission.ADMINISTRATOR;
		Permission botReq = Permission.MANAGE_CHANNEL;

		if (!sender.hasPermission(req)) {
			Utilities.sendEmbed(channel, Color.RED, "Only admins may use this command!");
			return;
		}

		if (!event.getSelfMember().hasPermission(botReq)) {
			try {
				Utilities.sendEmbed(channel, Color.RED, "Solent doesn't have enough permissions to do this.");
			} catch (InsufficientPermissionException ignored) {}
		}

		EnumSet<Permission> denyPublic = EnumSet.of(Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL);
		Role publicRole = event.getGuild().getPublicRole();

		event.getGuild().createTextChannel("logs")
				.setTopic("Log Channel")
				.addPermissionOverride(publicRole, null, denyPublic)
				.queue(result -> {
					Utilities.sendEmbed(channel, Color.GREEN, "Successfully created the log channel **" + result.getAsMention() + "**!");
					db.updateLogChannel(event, result);
		});

	}
}
