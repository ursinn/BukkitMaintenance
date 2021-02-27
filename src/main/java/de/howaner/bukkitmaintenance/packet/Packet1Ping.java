package de.howaner.bukkitmaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet1Ping extends Packet {

    private long a; //Time

    @Override
    public void read(DataInputStream stream) throws Exception {
        this.a = stream.readLong();
    }

    @Override
    public void write(DataOutputStream stream) throws Exception {
        stream.writeLong(this.a);
    }

    @Override
    public int getPacketID() {
        return 0x01;  //0x01 = 1
    }
}
