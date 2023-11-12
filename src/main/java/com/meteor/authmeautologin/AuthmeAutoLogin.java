package com.meteor.authmeautologin;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.security.PasswordSecurity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class AuthmeAutoLogin extends JavaPlugin implements Listener {

    public static final String channel = "autologin:main";

    public static AuthmeAutoLogin INSTANCE;

    public AuthmeAutoLogin(){
        INSTANCE = this;
    }

    private Metrics metrics;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getMessenger().registerOutgoingPluginChannel(this, channel);
        getServer().getMessenger().registerIncomingPluginChannel(this,channel,new PacketHandler());
        getServer().getPluginManager().registerEvents(this, this);
        metrics = new Metrics(this,20280);
        getLogger().info("有任何问题欢迎加群653440235反馈");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @EventHandler
    void onJoinServer(PlayerJoinEvent joinEvent){
        Player player = joinEvent.getPlayer();

        try {
            Class<? extends CommandSender> senderClass = player.getClass();
            Method addChannel = senderClass.getDeclaredMethod("addChannel", String.class);
            addChannel.setAccessible(true);
            addChannel.invoke(player, channel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskLater(this,()->{
            PacketHandler.sendRequestPasswordPack(player);
        },20L);
    }




}
