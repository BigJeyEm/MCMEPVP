package co.mcme.pvp.listeners;

import co.mcme.pvp.MCMEPVP;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class tagListener {

    @EventHandler
    public void onNameTag(PlayerReceiveNameTagEvent event) {
        Player target = event.getNamedPlayer();
        String oldname = target.getName();
        String newname = oldname;
        if (oldname.length() == 15) {
            int len = oldname.length();
            int removechars = len -1;
            newname = oldname.substring(0, removechars);
        }
        if (oldname.length() == 16) {
            int len = oldname.length();
            int removechars = len -2;
            newname = oldname.substring(0, removechars);
        }
        if (MCMEPVP.getPlayerStatus(target).equalsIgnoreCase("blue")){
            event.setTag(ChatColor.BLUE + newname);
        }
        if (MCMEPVP.getPlayerStatus(target).equalsIgnoreCase("red")){
            event.setTag(ChatColor.RED + newname);
        }
    }
}