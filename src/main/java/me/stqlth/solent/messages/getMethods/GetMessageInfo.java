package me.stqlth.solent.messages.getMethods;

import me.stqlth.solent.config.SolentConfig;
import me.stqlth.solent.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.*;
import java.util.LinkedList;

public class GetMessageInfo {

    private SolentConfig solentConfig;
    private DebugMessages debugMessages;
    public GetMessageInfo(SolentConfig solentConfig, DebugMessages debugMessages) {
        this.solentConfig = solentConfig;
        this.debugMessages = debugMessages;
    }

    public String getPrefix(Guild guild) {

        LinkedList<Object> prefixes = new LinkedList<>();

        try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            int gSettingsId=0;

            ResultSet id = statement.executeQuery("CALL GetGuildSettingsId(" + guild.getId() + ")");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            ResultSet rs = statement.executeQuery("CALL GetPrefix(" + gSettingsId + ")");

            if (rs.next()) {
                prefixes.add(rs.getString("Prefix"));
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return prefixes.getFirst().toString();
    }
    public String getBirthdayChannelId(GuildMessageReceivedEvent event){

        LinkedList<Object> channels = new LinkedList<>();

        try (Connection conn = DriverManager.getConnection(solentConfig.getDbUrl(), solentConfig.getDbUser(), solentConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            int gSettingsId=0;

            ResultSet id = statement.executeQuery("CALL GetGuildSettingsId(" + event.getGuild().getId() + ")");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            ResultSet rs = statement.executeQuery("CALL GetBirthdayChannel(" + gSettingsId + ")");

            if (rs.next()) {
                channels.add(rs.getString("BirthdayChannel"));
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return channels.getFirst().toString();
    }
}
