package ru.tikskit.service;

import ru.tikskit.domain.GameResult;
import ru.tikskit.domain.PlayerInfo;

public interface GameResultOutput {
    void showResult(PlayerInfo playerInfo, GameResult gameResult);
}
