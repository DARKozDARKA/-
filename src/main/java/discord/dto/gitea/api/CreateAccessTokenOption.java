package discord.dto.gitea.api;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateAccessTokenOption {
    private final String name;
    private final List<String> scopes = List.of("all", "sudo");
}
