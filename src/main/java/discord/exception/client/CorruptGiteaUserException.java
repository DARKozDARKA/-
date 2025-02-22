package discord.exception.client;

import discord.logging.LogMessage;

public class CorruptGiteaUserException extends ClientGuildAwareException {
    public CorruptGiteaUserException(LogMessage logMessage, String guildId) {
        super(logMessage, guildId);
    }

    public CorruptGiteaUserException(LogMessage logMessage, String guildId, Throwable cause) {
        super(logMessage, guildId, cause);
    }
}
