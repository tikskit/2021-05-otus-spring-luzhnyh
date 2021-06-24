package ru.tikskit.service;

import ru.tikskit.domain.PlayerInfo;

public interface LoginedPlayedInfo {
    boolean isPlayerLogined();
    PlayerInfo getPlayerInfo();
    void setPlayerInfo(PlayerInfo playerInfo);
}
