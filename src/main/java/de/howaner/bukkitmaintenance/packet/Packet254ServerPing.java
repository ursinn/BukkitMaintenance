package de.howaner.bukkitmaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet254ServerPing extends Packet {
    public byte a;

    @Override
    public void read(DataInputStream stream) throws Exception {
        this.a = stream.readByte();
        this.a = (byte) 1;
    }

    @Override
    public void write(DataOutputStream stream) throws Exception {
        stream.writeByte(this.a);
    }

    @Override
    public int getPacketID() {
        return 0xFE;     //0xFE = 254
    }

}