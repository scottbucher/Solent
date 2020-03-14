package me.stqlth.solent.events;

import me.stqlth.solent.utils.DatabaseMethods;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;


public class ReactionAdd extends ListenerAdapter {
    private DatabaseMethods db;

    public ReactionAdd(DatabaseMethods databaseMethods) {
        this.db = databaseMethods;
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (Objects.requireNonNull(event.getUser()).isBot()) return;

        TextChannel eventTextChannel = event.getTextChannel();

        Guild guild = event.getGuild();

        TextChannel verifyChannel = guild.getTextChannelById(db.getVerifyChannel(guild));
        if (verifyChannel == null) return;


        if (eventTextChannel == verifyChannel) {

            Role memberRole = event.getGuild().getRoleById(db.getVerifiedRole(guild));
            if (memberRole == null) {
                try {
                    event.getReaction().removeReaction(event.getUser()).queue();
                } catch (InsufficientPermissionException ignored) {}
                return;
            }

            Member reactor = event.getMember();
            if (reactor == null) return;
            try {
                event.getGuild().addRoleToMember(reactor, memberRole).queue();
            } catch (Exception ex) {
                try {
                    event.getReaction().removeReaction(event.getUser()).queue();
                } catch (InsufficientPermissionException ignored) {}
            }
        }

    }

}
