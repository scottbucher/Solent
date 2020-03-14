package me.stqlth.solent.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.solent.messages.discordOut.StaffMessages;
import me.stqlth.solent.utils.DatabaseMethods;
import me.stqlth.solent.utils.Utilities;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class SetVerifiedRole extends Command {

	private DatabaseMethods db;
	private StaffMessages staffMessages;

	public SetVerifiedRole(DatabaseMethods databaseMethods, StaffMessages staffMessages) {
		this.name = "setVerifiedRole";
		this.help = "Set the verified role";
		this.arguments = "<@role/role name>";
		this.guildOnly = true;
		this.hidden = true;

		this.db = databaseMethods;
		this.staffMessages = staffMessages;
	}

	@Override
	protected void execute(CommandEvent event) {
		TextChannel channel = event.getTextChannel();

		Member sender = event.getMember();
		Permission req = Permission.ADMINISTRATOR;

		if (!sender.hasPermission(req)) {
			Utilities.sendEmbed(channel, Color.RED, "Only admins may use this command!"); //Only admins may use this command
			return;
		}

		String[] args = event.getMessage().getContentRaw().split(" ");

		if (args.length != 2) {
			staffMessages.sendErrorMessage(event.getGuild(), channel, getName(), arguments);
			return;
		}

		Role verifiedRole;

		try {
			verifiedRole = event.getMessage().getMentionedRoles().get(0);
		} catch (IndexOutOfBoundsException e) {
			verifiedRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains(args[1].toLowerCase())).findFirst().orElse(null);
		}

		if (verifiedRole == null) {
			Utilities.sendEmbed(channel, Color.RED, "That role cannot be found.");
			return;
		}

		db.updateVerifiedRole(event, verifiedRole);
		Utilities.sendEmbed(channel, Color.GREEN, "Successfully set the verified role to **" + verifiedRole.getAsMention() + "**!");
	}
}
