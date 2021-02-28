package de.howaner.bukkitmaintenance.packet;

import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Disconnect Packet for 1.7
 *
 * @author franz
 */
public class Packet0Disconnect extends Packet {

    @Setter
    private String a;

    @Override
    public void read(DataInputStream stream) throws IOException {
        this.a = this.readVarIntString(stream, 256);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        this.writeVarIntString(stream, this.a);
    }

    @Override
    public int getPacketID() {
        return 0x00;  //0x00 = 0
    }
}
