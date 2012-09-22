package co.mcme.pvp.gametypes;

import co.mcme.pvp.MCMEPVP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import co.mcme.pvp.Game;

public class TDMGame extends Game{
	private int RedMates = 0;
	private int BlueMates = 0;
        public Plugin plugin;

    public TDMGame() {
        MCMEPVP.GameStatus = 1;
        //Broadcast
	Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "The next Game starts in a few seconds!");
	Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "GameType is "+ChatColor.DARK_PURPLE+"Team Deathmatch"+ChatColor.GREEN+" on Map "+ChatColor.DARK_PURPLE+MCMEPVP.PVPMap+"!");
	Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "All Participants will be assigned to a team and teleported to their spawn!");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("MCMEPVP"), new Runnable() {
        public void run() {
            for (Player user : Bukkit.getOnlinePlayers()) {
                //TODO team assigning more random
                //Assign Teams
                if (MCMEPVP.getPlayerStatus(user).equals("participant")){
                    if(BlueMates > RedMates){
                        addTeam(user,"red");
                    }else{
                        if(BlueMates < RedMates){
                            addTeam(user,"blue");
                        }else{
                            boolean random = (Math.random() < 0.5);
                            if(random == true){
                                addTeam(user,"red");
                            }else{
                                addTeam(user,"blue");
                            }
                        }
                    }
                    //heal
                    user.setHealth(20);
                    user.setFoodLevel(20);
                    //Give Weapons and Armour
                    PlayerInventory inv = user.getInventory();
                    inv.setItemInHand(new ItemStack(276));//Sword
                    inv.setChestplate(new ItemStack(311));//Armour
                    inv.setLeggings(new ItemStack(312));//Leggins
                    inv.setBoots(new ItemStack(313));//Boots
                    inv.addItem(new ItemStack(261),new ItemStack(262, 32));//Bow + Arrows
                }
                //Teleport User
                Vector vec = MCMEPVP.Spawns.get(MCMEPVP.getPlayerStatus(user));
                Location loc = new Location(MCMEPVP.PVPWorld, vec.getX(), vec.getY(), vec.getZ());
                user.teleport(loc);
            }
            //Broadcast
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "The Fight begins!");
        }}, 100L);
    }

	protected void addTeam(Player player, String Team) {
		if(Team.equals("red")){
                        player.sendMessage(ChatColor.YELLOW + "You're now in Team "+ChatColor.RED+"RED"+ChatColor.YELLOW+"!");
			RedMates++;
			MCMEPVP.setPlayerStatus(player, Team, ChatColor.RED);
			player.getInventory().setHelmet(new ItemStack(35, 1, (short) 0, (byte) 14));
		}else{
			if(Team.equals("blue")){
                                player.sendMessage(ChatColor.YELLOW + "You're now in Team "+ChatColor.BLUE+"BLUE"+ChatColor.YELLOW+"!");
				BlueMates++;
				MCMEPVP.setPlayerStatus(player, Team, ChatColor.BLUE);
				player.getInventory().setHelmet(new ItemStack(35, 1, (short) 0, (byte) 11));
			}
		}
	}

	public void onPlayerjoinServer(PlayerLoginEvent event) {
                Vector vec = MCMEPVP.Spawns.get("spectator");
                Location loc = new Location(MCMEPVP.PVPWorld, vec.getX(), vec.getY(), vec.getZ());
                event.getPlayer().teleport(loc);		
	}

	public void onPlayerleaveServer(PlayerQuitEvent event) {
		String OldTeam = MCMEPVP.getPlayerStatus(event.getPlayer());
		if(OldTeam.equals("red")){
			RedMates--;
		} else {
			if(OldTeam.equals("blue")){
				BlueMates--;
			} else {
				//Error
			}
		}
                checkGameEnd();
	}

	public void onPlayerdie(PlayerDeathEvent event) {
            Player player = event.getEntity();
            String Status = MCMEPVP.getPlayerStatus(player);
            if(Status.equals("spectator")){
                event.setDeathMessage(ChatColor.YELLOW + "Spectator " + player.getName() + " was tired watching this fight!");
            }
            if(Status.equals("red")){
                RedMates--;
		event.setDeathMessage(ChatColor.RED + "Team Red " + ChatColor.YELLOW + "lost " + player.getName());
		event.getDrops().add(new ItemStack(364, 1));
            }
            if(Status.equals("blue")){
                BlueMates--;
		event.setDeathMessage(ChatColor.BLUE + "Team Blue " + ChatColor.YELLOW + "lost " + player.getName());
		event.getDrops().add(new ItemStack(364, 1));
            }
            MCMEPVP.setPlayerStatus(event.getEntity(),"spectator", ChatColor.WHITE);
            checkGameEnd();		
	}

	public void onPlayerhit(EntityDamageByEntityEvent event) {
	} 

    private void checkGameEnd() {
            if(BlueMates == 0){
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Team "
                        + ChatColor.RED + "Red" + ChatColor.GREEN + " wins!");
		MCMEPVP.resetGame();
            }else if(RedMates == 0){
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Team "
                        + ChatColor.BLUE + "Blue" + ChatColor.GREEN + " wins!");
		MCMEPVP.resetGame();
            }
    }
}