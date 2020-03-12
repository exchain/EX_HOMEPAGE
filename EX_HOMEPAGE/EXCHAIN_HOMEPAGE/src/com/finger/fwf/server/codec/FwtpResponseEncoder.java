package com.finger.fwf.server.codec;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import com.finger.agent.util.AgentUtil;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

/**
 * A {@link MessageEncoder} that encodes {@link FwtpResponseEncoder}.
 */
public class FwtpResponseEncoder extends FwtpHeader implements MessageEncoder<FwtpMessage> {
    private static Logger logger = Logger.getLogger(FwtpResponseEncoder.class);
    private static final CharsetEncoder _encoder = Charset.defaultCharset().newEncoder();

    public FwtpResponseEncoder() { }

    public void encode(IoSession session, FwtpMessage message, ProtocolEncoderOutput out) throws Exception {

        IoBuffer buf = IoBuffer.allocate(256);
        // Enable auto-expand for easier encoding
        buf.setAutoExpand(true);

        try {
            Map<String, String> mapHeader = message.getHeader();
            String responseData = AgentUtil.nvl(message.getResponseDataString());

            StringBuilder strbuild = new StringBuilder();

            String strValue = "";
            int nLength = 0;
            int nDSize = 0;

            // 0. INITIAL - FWTP
            strbuild.append( _C_INITIAL );

            // 1. DATA_LEN
            nLength = responseData.getBytes().length;
            nDSize  = FWTP_HEADER_SIZE[__DATA_LEN];
            strbuild.append(AgentUtil.fillNumericString(Integer.toString(nLength), nDSize));
            // 2. ROW_COUNT
            nLength = message.getResponseDataSize();
            nDSize  = FWTP_HEADER_SIZE[__ROW_COUNT];
            strbuild.append(AgentUtil.fillNumericString(Integer.toString(nLength), nDSize));

            // __REQUEST_BY 항목부터 끝까지 셋팅
            for (int i=__REQUEST_BY; i<FWTP_HEADER_SIZE.length; i++) {
                strValue = mapHeader.get(FWTP_HEADER_INFO[i]);
                nDSize   = FWTP_HEADER_SIZE[i];

                if (FWTP_HEADER_TYPE[i].toUpperCase().equals("X")) {    // 문자열처리
                    strbuild.append(AgentUtil.fillSpaceString(strValue, nDSize));
                }
                else {  // Number 처리
                    strbuild.append(AgentUtil.fillNumericString(strValue, nDSize));
                }
            }
            StringBuffer sbInfo = new StringBuffer();
            sbInfo.append("\nResponse Message Send   =======================================================\n");
            sbInfo.append("Client Ip       [").append(session.getLocalAddress().toString()).append("]\n");
            sbInfo.append("Response Header [").append(strbuild.toString().getBytes().length).append("] [").append(strbuild.toString()).append("]\n");
            sbInfo.append("Response DataLen[").append(responseData.getBytes().length).append("] / CharsetEncoder ["+ _encoder.charset() +"]\n");
            if (logger.isInfoEnabled())
                logger.info(sbInfo.toString());

            buf.putString(strbuild.toString(), _encoder);
            /*buf.putString(responseData, _encoder);*/
            buf.put( responseData.getBytes() );
        }
        catch (CharacterCodingException ex) {
            logger.error(ex);
        }

        buf.flip();
        out.write(buf);
    }
}