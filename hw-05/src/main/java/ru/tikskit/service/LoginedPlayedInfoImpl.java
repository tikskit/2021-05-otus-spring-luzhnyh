package ru.tikskit.service;

import org.springframework.stereotype.Component;
import ru.tikskit.domain.PlayerInfo;

@Component
public class LoginedPlayedInfoImpl implements LoginedPlayedInfo{

    private PlayerInfo playerInfo;

    @Override
    public boolean isPlayerLogined() {
        return playerInfo != null;
    }

    @Override
    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    @Override
    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }
}
