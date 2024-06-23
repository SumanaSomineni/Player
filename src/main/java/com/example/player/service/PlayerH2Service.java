
package com.example.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.player.model.PlayerRowMapper;
import com.example.player.model.Player;
import com.example.player.repository.PlayerRepository;

@Service
public class PlayerH2Service implements PlayerRepository {
  @Autowired
  private JdbcTemplate db;

  @Override
  public ArrayList<Player> getPlayers() {
    List<Player> playerList = db.query("SELECT * FROM player", new PlayerRowMapper());
    ArrayList<Player> players = new ArrayList<>(playerList);
    return players;
  }

  @Override
  public Player getPlayerById(int playerId) {
    try {
      Player player = db.queryforObject("SELECT * FROM player where playerId=", new PlayerRowMapper(), playerId);
      return player;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

  }

  @Override
  public Player addPlayer(Player player) {
    db.update("insert into player(playerName,jerseyNumber,role) values (?,?,?)", player.getPlayerName(),
        player.getJerseyNumber(),
        player.getRole());
    Player savedPlayer = db.queryForObject("select * from player where playerName=? and jerseyNumber=? and role=?",
        new PlayerRowMapper(),
        player.getPlayerName(), player.getJerseyNumber(),player.getRole());
    return savedPlayer;
  }

  @Override
  public Player updatePlayer(int playerId, Player player) {
    if (player.getPlayerName != null) {
      db.update("update player set playerName=? where playerId=?", player.getPlayerName, playerId);
    }
    if (player.getJerseyNumber != null) {
      db.update("update player set jerseyNumber=? where playerId=?", player.getJerseyNumber, playerId);
    }
    if (player.getRole != null) {
      db.update("update player set role=? where playerId=?", player.getRole, playerId);
    }
    return getPlayerById(playerId);
  }

  @Override
  public void deletePlayer(int playerId) {
    db.update("delete from player where playerId=?", playerId);
  }

}