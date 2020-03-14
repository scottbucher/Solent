package me.stqlth.solent.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.solent.config.SolentConfig;
import me.stqlth.solent.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.*;

public class DatabaseMethods {

	private SolentConfig solentConfig;
	private DebugMessages debugMessages;

	public DatabaseMethods(SolentConfig solentConfig, DebugMessages debugMessages) {
		this.solentConfig = solentConfig;
		this.debugMessages = debugMessages;
	}

	public int getGuildSettingsId(Guild guild) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			ResultSet rs = statement.executeQuery("CALL GetGuildSettingsId(" + guild.getId() + ")");
			rs.next();
			return rs.getInt("GuildSettingsId");

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return 0;
	}

	public long getVerifyChannel(Guild guild) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(guild);


			ResultSet rs = statement.executeQuery("CALL GetVerifyChannel(" + guildSettingsId + ")");
			rs.next();
			return rs.getLong("BirthdayChannel");

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return 0;
	}

	public long getLogChannel(Guild guild) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(guild);


			ResultSet rs = statement.executeQuery("CALL GetLogChannel(" + guildSettingsId + ")");
			rs.next();
			return rs.getLong("BirthdayChannel");

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return 0;
	}

	public long getVerifiedRole(Guild guild) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(guild);


			ResultSet rs = statement.executeQuery("CALL GetVerifiedRole(" + guildSettingsId + ")");
			rs.next();
			return rs.getLong("TrustedRole");

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return 0;
	}

	public long getMutedRole(Guild guild) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(guild);


			ResultSet rs = statement.executeQuery("CALL GetMutedRole(" + guildSettingsId + ")");
			rs.next();
			return rs.getLong("TrustedRole");

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return 0;
	}

	public void updateVerifyChannel(CommandEvent event, TextChannel bdayChannel) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateVerifyChannel(" + bdayChannel.getId() + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public void updateLogChannel(CommandEvent event, TextChannel bdayChannel) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateLogChannel(" + bdayChannel.getId() + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public void clearVerifyChannel(CommandEvent event) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateVerifyChannel(" + 0 + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public void clearLogChannel(CommandEvent event) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateLogChannel(" + 0 + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public void updateVerifiedRole(CommandEvent event, Role bdayRole) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateVerifiedRole(" + bdayRole.getId() + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public void updateMutedRole(CommandEvent event, Role bdayRole) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateMutedRole(" + bdayRole.getId() + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public void clearVerifiedRole(CommandEvent event) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateVerifiedRole(" + 0 + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public void clearMutedRole(CommandEvent event) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(event.getGuild());

			statement.execute("CALL UpdateMutedRole(" + 0 + ", " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

	public boolean isGuildActive(Guild g) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			ResultSet check = statement.executeQuery("CALL IsGuildActive(" + g.getId() + ")");
			check.next();
			boolean alreadyExists = check.getBoolean("Active");

			if (alreadyExists) return true;

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return false;
	}

	public int getGuildId(Guild guild) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {
			ResultSet rs = statement.executeQuery("CALL GetGuildId(" + guild.getId() + ")");
			rs.next();
			return rs.getInt("GuildId");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return -1;
	}

	public boolean updatePrefix(Guild guild, String prefix) {

		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			int guildSettingsId = getGuildSettingsId(guild);

			statement.execute("CALL UpdatePrefix('" + prefix + "', " + guildSettingsId + ")");
		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
			return false;
		}
		return true;
	}

}
