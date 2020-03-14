package me.stqlth.solent.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.solent.utils.DatabaseMethods;
import me.stqlth.solent.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.util.EnumSet;

public class CreateVerifyChannel extends Command {

	private DatabaseMethods db;

	public CreateVerifyChannel(DatabaseMethods databaseMethods) {
		this.name = "createVerify";
		this.help = "Creates the verify channel";
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

		EnumSet<Permission> grantPublic = EnumSet.of(Permission.VIEW_CHANNEL), //Application Permissions
				denyPublic = EnumSet.of(Permission.MESSAGE_WRITE);
		Role publicRole = event.getGuild().getPublicRole();

		event.getGuild().createTextChannel("verify")
				.setTopic("User Verification")
				.addPermissionOverride(publicRole, grantPublic, denyPublic)
				.queue(result -> {
					Utilities.sendEmbed(channel, Color.GREEN, "Successfully created the verify channel **" + result.getAsMention() + "**!");
					db.updateVerifyChannel(event, result);

					SelfUser bot = event.getJDA().getSelfUser();
					String botIcon = bot.getAvatarUrl();
					EmbedBuilder builder = new EmbedBuilder();

					builder.setTitle("User Verification")
							.setColor(Color.decode("#EA2027"))
							.setDescription("Please verify your account by reacting with the \uD83D\uDD13 emote on this message. " +
									"You will receive access to the rest of the discord once this has been completed." +
									"\n\nThis is to prevent abuse/exploitation of the discord.")
							.setFooter("If you encounter any issues please contact " + event.getAuthor().getName()
									+ " or an Administrator", event.getAuthor().getAvatarUrl())
							.setThumbnail(botIcon);
					result.sendMessage(builder.build()).queue(r -> {
						r.addReaction("\uD83D\uDD13").queue();
					});
		});

	}
}
