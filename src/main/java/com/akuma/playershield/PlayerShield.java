package com.akuma.playershield;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

public class PlayerShield extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("PlayerShield: Anti-Cheat for AkumaPlay is Online.");
    }

    // COMBAT REACH CHECK
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;

        double distance = attacker.getLocation().distance(event.getEntity().getLocation());
        boolean isBedrock = FloodgateApi.getInstance().isFloodgatePlayer(attacker.getUniqueId());

        // Bedrock reach is naturally longer; Java is short and precise.
        double maxReach = isBedrock ? 5.2 : 3.8;

        if (distance > maxReach) {
            event.setCancelled(true);
            attacker.sendMessage("§c§lSHIELD §8» §7Combat flag: Reach too far.");
        }
    }

    // MOVEMENT SPEED CHECK
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying() || player.isInsideVehicle() || player.isGliding()) return;

        double dist = event.getFrom().distance(event.getTo());
        boolean isBedrock = FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());

        // Java has stable packets (0.6); Bedrock "rubber-bands" more (0.85).
        double speedLimit = isBedrock ? 0.85 : 0.65;

        if (dist > speedLimit) {
            event.setTo(event.getFrom()); // Pull back cheaters
            player.sendMessage("§c§lSHIELD §8» §7Movement flag: Moving too fast.");
        }
    }
            }
