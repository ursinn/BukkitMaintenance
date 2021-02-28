package de.howaner.bukkitmaintenance.packet;

import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet254ServerPing extends Packet {

    @Getter
    private byte a;

    @Override
    public void read(DataInputStream stream) throws IOException {
        this.a = stream.readByte();
        this.a = (byte) 1;
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeByte(this.a);
    }

    @Override
    public int getPacketID() {
        return 0xFE;     //0xFE = 254
    }
}
