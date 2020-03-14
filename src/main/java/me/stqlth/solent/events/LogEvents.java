package me.stqlth.solent.events;

import me.stqlth.solent.messages.discordOut.LogMessages;
import me.stqlth.solent.utils.DatabaseMethods;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.Objects;

public class LogEvents extends ListenerAdapter {

	private ShardManager client;
	private DatabaseMethods db;
	private LogMessages logMessages;

	public LogEvents(ShardManager client, LogMessages logMessages, DatabaseMethods databaseMethods) {
		this.client = client;
		this.db = databaseMethods;
		this.logMessages = logMessages;
	}

	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {

		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.nicknameChanged(event, logChannel);
	}
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.memberJoin(event, logChannel);
	}
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		Objects.requireNonNull(client.getGuildById(event.getGuild().getIdLong())).retrieveBan(event.getUser()).queue(
				(ban) -> {},
				(error) ->{
					logMessages.memberLeave(event, logChannel);
				}
		);
	}
	public void onGuildBan(GuildBanEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.memberBan(event, logChannel);
	}
	public void onGuildUnban(GuildUnbanEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.memberUnban(event, logChannel);
	}
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.memberAddRole(event, logChannel);
	}
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.memberRemoveRole(event, logChannel);
	}
	public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.messageDelete(event, logChannel, client);
	}

	public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
		TextChannel logChannel = event.getGuild().getTextChannelById(db.getLogChannel(event.getGuild()));
		if (logChannel == null) return;

		logMessages.messageChange(event, logChannel);
	}

}
