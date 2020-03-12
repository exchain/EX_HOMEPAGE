package com.finger.fwf.server.codec;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

/**
 * A {@link MessageDecoderAdapter} that decodes {@link FwtpRequestDecoder}.
 */
public class FwtpRequestDecoder extends MessageDecoderAdapter {
    private static Logger logger = Logger.getLogger(FwtpRequestDecoder.class);

    public FwtpRequestDecoder() { }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        int remain = in.remaining();

        // Header Size 점검 => Header Data 길이가 존재 => 해당 요청의 전체 길이를 구하고 그 만큼 전송받는다.
        if (remain < FwtpHeader._HEADER_SIZE) {
            return MessageDecoderResult.NEED_DATA;
        }

        // 2. Data length
        StringBuilder stringBuilder = new StringBuilder();
        int nOffsetDataLen = FwtpHeader.getOffset(FwtpHeader.FWTP_HEADER_INFO[FwtpHeader.__DATA_LEN]);
        for (int i = nOffsetDataLen; i < (nOffsetDataLen+FwtpHeader.FWTP_HEADER_SIZE[FwtpHeader.__DATA_LEN]); i++) {
            stringBuilder.append(new String(new byte[] { in.get(i) }));
        }

        // 데이터 영역의 길이
        int lenContent = 0;
        try {
            lenContent = Integer.parseInt(stringBuilder.toString().trim());
        }
        catch(NumberFormatException e) {
            return MessageDecoderResult.NOT_OK; // 에러를 발생시키고 해당 작업을 종료함
        }

        if (remain >= FwtpHeader._HEADER_SIZE + lenContent) {
            return MessageDecoderResult.OK;
        }
        else {
            return MessageDecoderResult.NEED_DATA;
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        //logger.debug("decode() " + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        // Try to decode body
        FwtpMessage fmsg = decodeBody(in);

        // Return NEED_DATA if the body is not fully read.
        if (fmsg == null) {
            return MessageDecoderResult.NEED_DATA;
        }
        else {
            // Header 항목값이 올바른지 검사
            if (!fmsg.validateHeaderMap(fmsg.getHeader())) {
                return MessageDecoderResult.NOT_OK;
            }
        }
        out.write(fmsg);

        return MessageDecoderResult.OK;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
        logger.debug("finishDecode() " + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }


    /**
     * 전송된 buffer 를 필요한 정보로 파싱한다
     * @param in 전송된 buffer
     * @return FwtpMessage
     */
    private FwtpMessage decodeBody(IoBuffer in) {
        //logger.debug("decodeBody() " + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        FwtpMessage fwmsg = new FwtpMessage();

        try {
            fwmsg.setHeader( decodeHeader(in) );
        }
        catch (CharacterCodingException cex) {
            logger.error(cex.getMessage(), cex);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return fwmsg;
    }

    /**
     * 헤더정보를 분석하여 각 필드의 정보를 Map에 담는다
     * @param in 입력 Stream
     * @return Header [Key:Value] Map 정보
     * @throws CharacterCodingException
     */
    private LinkedHashMap<String, String> decodeHeader(IoBuffer in) throws CharacterCodingException {
        in.position(in.position() + FwtpHeader._HEADER_SIZE);

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        int length = FwtpHeader.FWTP_HEADER_INFO.length;
        int offset = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("\nRequest Message Receive -------------------------------------------------------\n");
        for (int ii=0; ii<length; ii++) {
            String inputHeaderItem = getInputHeaderItem(in, offset, FwtpHeader.FWTP_HEADER_SIZE[ii]);

            sb.append( FwtpHeader.FWTP_HEADER_INFO[ii]);
            if (FwtpHeader.FWTP_HEADER_INFO[ii].length() < 8) sb.append("\t");
            sb.append("\t=> [").append(inputHeaderItem).append("]\n");

            map.put(FwtpHeader.FWTP_HEADER_INFO[ii], inputHeaderItem.trim());

            offset += FwtpHeader.FWTP_HEADER_SIZE[ii];
        }

        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();

        int datalen = getDataLength(map);
        String data = in.getString(datalen, decoder);
        map.put(FwtpHeader._C__DATA_, data);

        if (data.getBytes().length <= 10000)
            sb.append("# "+ FwtpHeader._C__DATA_ +" #\t=> [").append(data).append("]\n");
        else
            sb.append("# "+ FwtpHeader._C__DATA_ +" #\t=> [").append(new String(data.getBytes(),0,10000)+"。。。Over 10,000bytes]").append("]\n");

        logger.info(sb.toString());

        return map;
    }

    /**
     * 파싱한 헤더정보에서 DATALEN 를 구한다.
     * @param _header   파싱이 완료된 헤더정보
     * @return          DATALEN
     */
    private int getDataLength(Map<String, String> _header) {
        String strDataLength = _header.get(FwtpHeader.FWTP_HEADER_INFO[FwtpHeader.__DATA_LEN]);
        return Integer.parseInt(strDataLength);
    }

    /**
     * 입력전문 에서 헤더 정보를 가져온다.
     * @param in        입력전문
     * @param offset    헤더정보 시작위치
     * @param len       헤더정보 길이
     * @return          헤더정보
     */
    private String getInputHeaderItem(IoBuffer in, int offset, int len) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=offset; i<offset+len; i++) {
            stringBuilder.append(new String(new byte[] { in.get(i) }));
        }
        return stringBuilder.toString();
    }
}