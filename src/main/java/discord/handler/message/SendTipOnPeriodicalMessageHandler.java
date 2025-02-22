package discord.handler.message;

import discord.exception.main.EmptyOptionalException;
import discord.handler.EventPredicates;
import discord.logging.LogMessage;
import discord.services.MessageChannelService;
import discord.structure.ChannelRole;
import discord.structure.EmbedBuilder;
import discord.utils.Notificator;
import discord4j.core.event.domain.message.MessageCreateEvent;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendTipOnPeriodicalMessageHandler implements MessageHandler {
    private static final int MIN_MESSAGE_LENGTH = 6;

    private final Notificator notificator = new Notificator();

    private final MessageChannelService channelService;
    private final EventPredicates eventPredicates;

    @Override
    public boolean filter(MessageCreateEvent event) {
        return Stream.of(event)
                        .filter(e -> eventPredicates.filterIfChannelExistsInSettings(e, ChannelRole.HELP))
                        .filter(eventPredicates::filterEmptyAuthor)
                        .filter(eventPredicates::filterBot)
                        .filter(this::messageIsEnoughLarge)
                        .filter(e -> notificator.isTime())
                        .filter(e -> eventPredicates.filterByChannelRole(event, ChannelRole.HELP))
                        .count()
                == 1;
    }

    private boolean messageIsEnoughLarge(MessageCreateEvent event) {
        return event.getMessage().getContent().length() > MIN_MESSAGE_LENGTH;
    }

    @Override
    public void handle(MessageCreateEvent event) {
        final var embed = EmbedBuilder.buildTipEmbed(notificator.getNotificationText());
        final var guild =
                event.getGuild().blockOptional().orElseThrow(() -> new EmptyOptionalException(LogMessage.ALERT_20072));
        final var helpChannel = channelService.getChannel(guild, ChannelRole.HELP);

        helpChannel.createMessage(embed).subscribe();
    }
}
