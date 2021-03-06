/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mcme.pvp.util;

import co.mcme.pvp.MCMEPVP;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.kitteh.tag.TagAPI;

/**
 *
 * @author meggawatts
 */
public class teamUtil {

    public static void setPlayerTeam(Player player, String status) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        MCMEPVP.PlayerStatus.put(player.getName(), status);
        if (status.equals("spectator") || status.equals("participant")) {
            player.setMetadata("god", new FixedMetadataValue(MCMEPVP.inst(), true));
        }
        if (!(status.equals("spectator") || status.equals("participant"))) {
            player.setMetadata("god", new FixedMetadataValue(MCMEPVP.inst(), false));
        }
        if (player.isOnline()) {
            TagAPI.refreshPlayer(player);
        }
        if (status != "spectator") {
            spectatorUtil.setParticipant(player);
        }
        if (status == "spectator") {
            spectatorUtil.setSpectator(player);
        }
    }

    public static String getPlayerTeam(Player player) {
        String status = "spectator";
        if (MCMEPVP.PlayerStatus.containsKey(player.getName())) {
            status = MCMEPVP.PlayerStatus.get(player.getName());
        }
        return status;
    }

    public static boolean isOnTeam(Player player) {
        boolean check = true;
        if (MCMEPVP.PlayerStatus.containsKey(player.getName())) {
            String status = teamUtil.getPlayerTeam(player);
            if (status.equals("spectator")) {
                check = false;
            }
            if (status.equals("participant")) {
                check = false;
            }
        } else {
            check = false;
        }
        return check;
    }
    
}
