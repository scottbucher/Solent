package me.stqlth.solent.messages.discordOut;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LogMessages {

	public void memberJoin(GuildMemberJoinEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getUser();

		LocalDate now = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

		builder.setColor(Color.GREEN)
				.setAuthor("Member Joined", null, target.getAvatarUrl())
				.setDescription("<@" + target.getId() + "> " + target.getName() + "#" + target.getDiscriminator())
				.setThumbnail(target.getAvatarUrl())
				.setFooter("User ID: " + target.getId() + " ♦ " + now.format(dtf));

		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}

	public void memberLeave(GuildMemberLeaveEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getUser();

		LocalDate now = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

		builder.setColor(Color.ORANGE)
				.setAuthor("Member Left", null, target.getAvatarUrl())
				.setDescription("<@" + target.getId() + "> " + target.getName() + "#" + target.getDiscriminator())
				.setThumbnail(target.getAvatarUrl())
		        .setFooter("User ID: " + target.getId() + " ♦ " + now.format(dtf));

		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}

	public void memberBan(GuildBanEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getUser();

		LocalDate now = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

		builder.setColor(Color.RED)
				.setAuthor("Member Banned", null, target.getAvatarUrl())
				.setDescription("<@" + target.getId() + "> " + target.getName() + "#" + target.getDiscriminator())
				.setThumbnail(target.getAvatarUrl())
				.setFooter("User ID: " + target.getId() + " ♦ " + now.format(dtf));

		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}
	public void memberUnban(GuildUnbanEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getUser();

		LocalDate now = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

		builder.setColor(Color.GREEN)
				.setAuthor("Member UnBanned", null, target.getAvatarUrl())
				.setDescription("<@" + target.getId() + "> " + target.getName() + "#" + target.getDiscriminator())
				.setThumbnail(target.getAvatarUrl())
				.setFooter("User ID: " + target.getId() + " ♦ " + now.format(dtf));

		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}
	public void memberAddRole(GuildMemberRoleAddEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getUser();
		Role role = event.getRoles().get(0);
		LocalTime now = LocalTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

		builder.setColor(Color.CYAN)
				.setAuthor(target.getName() + "#" + target.getDiscriminator(), null, target.getAvatarUrl())
				.setDescription(target.getAsTag() + " **was given the `" + role.getName() + "` role**");

		builder.setFooter("User ID: " + target.getId() + " ♦ " + "Today at " + now.format(dtf));
		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}
	public void memberRemoveRole(GuildMemberRoleRemoveEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getUser();
		Role role = event.getRoles().get(0);
		LocalTime now = LocalTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

		builder.setColor(Color.CYAN)
				.setAuthor(target.getName() + "#" + target.getDiscriminator(), null, target.getAvatarUrl())
				.setDescription(target.getAsTag() + " **was removed from the `" + role.getName() + "` role**");

		builder.setFooter("User ID: " + target.getId() + " ♦ " + "Today at " + now.format(dtf));
		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}
	public void messageDelete(GuildMessageDeleteEvent event, TextChannel logChannel, ShardManager client) {
		EmbedBuilder builder = new EmbedBuilder();
		LocalTime now = LocalTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

		builder.setColor(Color.decode("#FE901C"))
				.setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl())
				.setDescription("**Message deleted in** " + event.getChannel().getAsMention());
		builder.setFooter("Author: ? ♦ Message ID: " + event.getMessageId() + " ♦ " + "Today at " + now.format(dtf));
		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}
	public void messageChange(GuildMessageUpdateEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getAuthor();
		LocalTime now = LocalTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

		builder.setColor(Color.CYAN)
				.setAuthor(target.getName() + "#" + target.getDiscriminator(), null, target.getAvatarUrl())
				.setDescription("**Message edited in** " + event.getChannel().getAsMention() + " [Jump to Message](" + event.getMessage().getJumpUrl() + ")")
				.addField("After", event.getMessage().getContentRaw(), false);

		builder.setFooter("User ID: " + target.getId() + " ♦ " + "Today at " + now.format(dtf));
		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}

	public void nicknameChanged(GuildMemberUpdateNicknameEvent event, TextChannel logChannel) {
		EmbedBuilder builder = new EmbedBuilder();
		User target = event.getUser();
		LocalTime now = LocalTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

		builder.setColor(Color.CYAN)
				.setAuthor(target.getName() + "#" + target.getDiscriminator(), null, target.getAvatarUrl())
				.setDescription(target.getAsTag() + "'s **nickname changed**");

				try {
				builder.addField("Old", event.getOldValue(), false);
				} catch (IllegalArgumentException ex) {
					builder.addField("Old", "None", false);
				}

				try {
					builder.addField("New", event.getNewValue(), false);
				} catch (IllegalArgumentException ex) {
					builder.addField("New", "None", false);
				}
				builder.setFooter("User ID: " + target.getId() + " ♦ " + "Today at " + now.format(dtf));
		try {
			logChannel.sendMessage(builder.build()).queue();
		} catch (InsufficientPermissionException ignored) {}
	}






}
