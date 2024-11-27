package br.com.pulsemc.minecraft.lobby.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerData {
    private String uuid;
    private long firstJoin;
    private long onlineTime;
}