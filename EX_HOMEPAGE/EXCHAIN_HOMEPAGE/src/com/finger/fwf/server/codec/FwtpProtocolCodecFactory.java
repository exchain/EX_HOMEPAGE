package com.finger.fwf.server.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import com.finger.protocol.fwtp.FwtpMessage;

/**
 * Provides a protocol codec for FWTP Server.
 */
public class FwtpProtocolCodecFactory extends DemuxingProtocolCodecFactory {
    public FwtpProtocolCodecFactory() {
        super.addMessageDecoder(FwtpRequestDecoder.class);
        super.addMessageEncoder(FwtpMessage.class, FwtpResponseEncoder.class);
    }
}