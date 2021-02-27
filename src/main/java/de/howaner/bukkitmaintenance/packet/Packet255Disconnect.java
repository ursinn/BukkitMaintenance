package de.howaner.bukkitmaintenance.packet;

import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet255Disconnect extends Packet {

    @Setter
    private String a;

    @Override
    public void read(DataInputStream stream) throws Exception {
        this.a = this.readString(stream, 256);
    }

    @Override
    public void write(DataOutputStream stream) throws Exception {
        this.writeString(stream, this.a);
    }

    @Override
    public int getPacketID() {
        return 0xFF;  //0xFF = 255
    }
}
