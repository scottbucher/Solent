package me.stqlth.solent.events;

import me.stqlth.solent.config.SolentConfig;
import me.stqlth.solent.messages.debug.DebugMessages;
import me.stqlth.solent.utils.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;


public class GuildJoinLeave extends ListenerAdapter {
    private SolentConfig solentConfig;
    private DebugMessages debugMessages;

    public GuildJoinLeave(SolentConfig solentConfig, DebugMessages debugMessages) {
        this.solentConfig = solentConfig;
        this.debugMessages = debugMessages;
    }

    public void onGuildJoin(GuildJoinEvent event) {
        Guild g = event.getGuild();
        Logger.Info("Registering Guild: \"" + g.getName() + "\" (" + g.getId() + ")!");

        try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet check = statement.executeQuery("CALL DoesGuildAlreadyExist(" + g.getId() + ")");
            check.next();
            boolean alreadyExists = check.getBoolean("AlreadyExists");

            if (!alreadyExists) {
                statement.execute("CALL InsertGuildSettings('!')");
                ResultSet rs = statement.executeQuery("CALL SelectLastInsertID()");
                rs.next();
                int lastId = rs.getInt("LAST_INSERT_ID()");
                statement.execute("CALL InsertGuild(" + g.getId() + ", " + lastId + ", 1)");
            } else {
                statement.execute("CALL UpdateGuildActive(" + g.getId() + ", 1)");
            }
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
            return;
        }

        Logger.Info("Registered Guild: \"" + g.getName() + "\" (" + g.getId() + ")!");
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        Guild g = event.getGuild();
        Logger.Info("UnRegistering Guild \"" + g.getName() + "\" (" + g.getId() + ")!");

        try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            statement.execute("CALL UpdateGuildActive(" + g.getId() + ", 0)");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
        Logger.Info("UnRegistered Guild \"" + g.getName() + "\" (" + g.getId() + ")!");
    }
}
