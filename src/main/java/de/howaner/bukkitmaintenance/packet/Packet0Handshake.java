package de.howaner.bukkitmaintenance.packet;

import de.howaner.bukkitmaintenance.util.Varint;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Handshake for 1.7
 *
 * @author franz
 */
public class Packet0Handshake extends Packet {

    private int a; //Protocol Version
    private String b; //Server Address
    private int c; //Server Port

    @Getter
    private int d; //Next State ( 1 = Status | 2 = Login )

    @Override
    public void read(DataInputStream stream) throws IOException {
        this.a = Varint.readVarInt(stream);
        this.b = this.readVarIntString(stream, 255);
        this.c = stream.readUnsignedShort();
        this.d = Varint.readVarInt(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        Varint.writeVarInt(stream, this.a);
        this.writeVarIntString(stream, this.b);
        stream.writeShort(this.c);
        Varint.writeVarInt(stream, this.d);
    }

    @Override
    public int getPacketID() {
        return 0x00;  //0x00 = 0
    }
}
