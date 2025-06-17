package it.fulminazzo.yagl.utils.current;

import io.netty.channel.Channel;
import it.fulminazzo.yagl.utils.current.containers.Container;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityPlayerContainer {
    private final EntityPlayer.ServerGamePacketListenerImpl connection;

    private Container container;

    public EntityPlayerContainer(Channel channel) {
        this.connection = new EntityPlayer.ServerGamePacketListenerImpl(channel);
    }

}
