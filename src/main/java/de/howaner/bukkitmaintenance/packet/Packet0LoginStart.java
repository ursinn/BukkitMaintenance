package de.howaner.bukkitmaintenance.packet;

import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Login Start Paket for 1.7
 *
 * @author franz
 */
public class Packet0LoginStart extends Packet {

    @Getter
    private String a; //Playername

    @Override
    public void read(DataInputStream stream) throws IOException {
        this.a = this.readVarIntString(stream, 16);
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
