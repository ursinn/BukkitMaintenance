package de.howaner.bukkitmaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2Handshake extends Packet {

    private byte a; //Protocol Version
    private String b; //Username
    private String c; //Server Host
    private int d; //Server Port

    @Override
    public void read(DataInputStream stream) throws IOException {
        this.a = stream.readByte();
        this.b = this.readString(stream, 16);
        this.c = this.readString(stream, 255);
        this.d = stream.readInt();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeByte(this.a);
        this.writeString(stream, this.b);
        this.writeString(stream, this.c);
        stream.writeInt(this.d);
    }

    @Override
    public int getPacketID() {
        return 0x02;  //0x02 = 2
    }
}
