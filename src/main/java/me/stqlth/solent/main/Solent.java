package me.stqlth.solent.main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.stqlth.solent.commands.staff.SetPrefix;
import me.stqlth.solent.config.SolentConfig;
import me.stqlth.solent.main.GuildSettings.SettingsManager;
import me.stqlth.solent.messages.debug.DebugMessages;
import me.stqlth.solent.messages.discordOut.StaffMessages;
import me.stqlth.solent.messages.getMethods.GetMessageInfo;
import me.stqlth.solent.utils.DatabaseMethods;
import me.stqlth.solent.utils.Logger;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;

public class Solent {

	private static final String CONFIG_FILE = "botconfig.json";
	private static final String SUCCESS_EMOJI = "\uD83D\uDE03";
	private static final String WARNING_EMOJI = "\uD83D\uDE2E";
	private static final String ERROR_EMOJI = "\uD83D\uDE26";

	public static void main(String[] args) {
		try {
			startBot();
		} catch (IllegalArgumentException ex) {
			Logger.Warn("No loging details provided! Please provide a botToken in the config (" + CONFIG_FILE + ") file!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void startBot() throws IOException {
		Logger.Info("Application started.");

		Logger.Info("Loading config...");
		SolentConfig solentConfig = loadConfig();

		// Construct dependencies
		EventWaiter waiter = new EventWaiter();

		DebugMessages debugMessages = new DebugMessages();
		GetMessageInfo getMessageInfo = new GetMessageInfo(solentConfig, debugMessages);

		SettingsManager settingsManager = new SettingsManager(solentConfig, debugMessages);
		DatabaseMethods databaseMethods = new DatabaseMethods(solentConfig, debugMessages);
		StaffMessages staffMessages = new StaffMessages(getMessageInfo);

		Command[] commands = new Command[]{
				//CONFIG


				//INFO


				//UTILITIES
				new SetPrefix(databaseMethods, staffMessages)
		};

		// Create the client
		CommandClient client = createClient(solentConfig, settingsManager, commands);

		EventListener[] listeners = new EventListener[]{
				waiter,
		};

		// Start the shard manager
		ShardManager instance;

		Logger.Info("Starting shard manager...");
		try {
			instance = startShardManager(solentConfig, client, listeners);
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SetupDatabase(solentConfig, instance.getGuilds(), debugMessages);
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			Logger.Error("Error encountered while logging in. The bot token may be incorrect.", ex);
		}


	}


	private static SolentConfig loadConfig() throws IOException {
		Path configFilePath = new File(CONFIG_FILE).toPath();
		String configData = new String(Files.readAllBytes(configFilePath));
		JSONObject configJson = new JSONObject(configData);
		return new SolentConfig(configJson);
	}

	private static CommandClient createClient(SolentConfig solentConfig, SettingsManager settingsManager, Command[] commands) {
		CommandClientBuilder clientBuilder = new CommandClientBuilder();
		clientBuilder.setGuildSettingsManager(settingsManager)
				.useDefaultGame()
				.useHelpBuilder(false)
				.setOwnerId(solentConfig.getOwnerId())
				.setActivity(Activity.listening("Happy Birthday"))
				.setStatus(OnlineStatus.ONLINE)
				.setPrefix(solentConfig.getPrefix())
				.setEmojis(SUCCESS_EMOJI, WARNING_EMOJI, ERROR_EMOJI)
				.addCommands(commands);
		return clientBuilder.build();
	}

	private static ShardManager startShardManager(SolentConfig solentConfig, CommandClient client, EventListener[] listeners) throws LoginException {
		DefaultShardManagerBuilder shardManager = new DefaultShardManagerBuilder();

		return shardManager.setToken(solentConfig.getToken())
				.addEventListeners((Object[]) listeners)
				.addEventListeners(client)
				.build();
	}

	private static void SetupDatabase(SolentConfig solentConfig, List<Guild> guildList, DebugMessages debugMessages) {
		for (Guild check : guildList) {
			if (!guildExists(solentConfig, debugMessages, check)) {
				AddGuildToDatabase(solentConfig, check, debugMessages);
			}
		}
	}

	private static boolean guildExists(SolentConfig solentConfig, DebugMessages debugMessages, Guild g) {
		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			ResultSet check = statement.executeQuery("CALL DoesGuildAlreadyExist(" + g.getId() + ")");
			check.next();
			boolean alreadyExists = check.getBoolean("AlreadyExists");

			if (alreadyExists) return true;

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
		return false;
	}
	private static void AddGuildToDatabase(SolentConfig solentConfig, Guild g, DebugMessages debugMessages) {

		try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
			 Statement statement = conn.createStatement()) {

			statement.execute("CALL InsertGuildSettings('!')");
			ResultSet rs = statement.executeQuery("CALL SelectLastInsertID()");
			rs.next();
			int lastId = rs.getInt("LAST_INSERT_ID()");
			statement.execute("CALL InsertGuild(" + g.getId() + ", " + lastId + ", 1)");

		} catch (SQLException ex) {
			debugMessages.sqlDebug(ex);
		}
	}

}