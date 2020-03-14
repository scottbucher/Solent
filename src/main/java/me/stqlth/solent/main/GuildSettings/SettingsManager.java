package me.stqlth.solent.main.GuildSettings;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import me.stqlth.solent.config.SolentConfig;
import me.stqlth.solent.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;

public class SettingsManager implements GuildSettingsManager<GuildSettingsProvider> {

    private SolentConfig solentConfig;
    private DebugMessages debugMessages;

    public SettingsManager(SolentConfig solentConfig, DebugMessages debugMessages) {
        this.solentConfig = solentConfig;
        this.debugMessages = debugMessages;
    }

    @Override
    public GuildSettingsProvider getSettings(Guild guild) {
        return new me.stqlth.solent.main.GuildSettings.Settings(guild, solentConfig, debugMessages);
    }

}
