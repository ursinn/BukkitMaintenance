package de.howaner.bukkitmaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Ping extends Packet {

    private long a; //Time

    @Override
    public void read(DataInputStream stream) throws IOException {
        this.a = stream.readLong();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeLong(this.a);
    }

    @Override
    public int getPacketID() {
        return 0x01;  //0x01 = 1
    }
}
