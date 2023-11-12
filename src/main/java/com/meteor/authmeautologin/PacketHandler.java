package com.meteor.authmeautologin;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.api.v3.AuthMePlayer;
import fr.xephi.authme.libs.javax.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

public class PacketHandler implements PluginMessageListener {
    public static void sendRequestPasswordPack(Player player) {
        String msg = "debug: request password";
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = Unpooled.buffer(bytes.length + 1);
        buf.writeByte(222);
        buf.writeBytes(bytes);
        player.sendPluginMessage(AuthmeAutoLogin.INSTANCE,AuthmeAutoLogin.channel,buf.array());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(channel.equalsIgnoreCase(AuthmeAutoLogin.channel)){
            String password = new String(message,StandardCharsets.UTF_8);
            if(AuthMeApi.getInstance().isRegistered(player.getName())){
                if(AuthMeApi.getInstance().checkPassword(player.getName(),password)){
                    AuthMeApi.getInstance().forceLogin(player);
                }else player.kickPlayer("密码错误");
            }else {
                AuthMeApi.getInstance().registerPlayer(player.getName(),password);
                if(AuthMeApi.getInstance().isRegistered(player.getName()))
                    AuthMeApi.getInstance().forceLogin(player);
                else player.kickPlayer("注册失败");
            }
        }
    }
}
