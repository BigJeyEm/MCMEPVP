package co.mcme.pvp.listeners;

import co.mcme.pvp.MCMEPVP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatListener implements Listener {

    public chatListener(MCMEPVP instance) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            String senderTeam = MCMEPVP.getPlayerTeam(event.getPlayer());
            String receiverStatus = MCMEPVP.getPlayerTeam(p);
            String label = "";
            if (senderTeam.equals("fighter")) {
                label = ChatColor.DARK_GREEN + "Fighter ";
            }
            if (senderTeam.equals("red")) {
                label = ChatColor.RED + "Team Red ";
            }
            if (senderTeam.equals("blue")) {
                label = ChatColor.BLUE + "Team Blue ";
            }
            if (senderTeam.equals("participant")) {
                label = ChatColor.GREEN + "Participant ";
            }
            if (receiverStatus.equals("spectator") || receiverStatus.equals("participant") || receiverStatus.equals("fighter") || receiverStatus.equals(senderTeam)) {
                p.sendMessage(label + event.getPlayer().getName() + ": " + ChatColor.WHITE + event.getMessage());
            }
        }
    }
}
