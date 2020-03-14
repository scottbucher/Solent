package me.stqlth.solent.main.GuildSettings;

import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import me.stqlth.solent.config.SolentConfig;
import me.stqlth.solent.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

public class Settings implements GuildSettingsProvider {

    private final Guild guild;
    private Collection<String> prefixes;
    private SolentConfig solentConfig;
    private DebugMessages debugMessages;

    public Settings(Guild guild, SolentConfig solentConfig, DebugMessages debugMessages) {
        this.guild = guild;
        this.prefixes = new LinkedList<>();
        this.solentConfig = solentConfig;
        this.debugMessages = debugMessages;
    }

    @Override
    public Collection<String> getPrefixes() {


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
        return prefixes;
    }
}
