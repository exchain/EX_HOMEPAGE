package com.finger.agent.scrap.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

public class ScrapTools {
    private static Logger logger = Logger.getLogger(ScrapTools.class);

    /**
     * raw data를 GZip으로 압축한 후 base64로 encoding 한다.
     * @param rawData
     * @return
     */
    public static String compressGZip(String rawData) throws Exception {

        String zipData = null;

        try {
            byte [] data = null;

            if ( rawData == null || rawData.equals("") )
                data = "".getBytes();
            else
                data = rawData.getBytes();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream( bout );

            gzipOutputStream.write( data, 0, data.length );

            gzipOutputStream.finish();
            gzipOutputStream.close();
            bout.close();

            zipData = new String(bout.toByteArray());

        } catch (Exception ex) {
            logger.debug(" ICMSStream dataCompressGZip Error :\n"+ ex.getMessage() );
            logger.debug(" ICMSStream Error rawData:"+ rawData );
        }

        return zipData;
    }

    /**
     * 압축된 데이터 해제
     * @param gzipedData
     * @return
     */
    public static String unGZip(String gzipedData)
        throws Exception {
        ByteArrayOutputStream bout = null;
        try {
            byte [] decodedData = null;

            if (gzipedData == null || gzipedData.equals("") )
                return "";

            GZIPInputStream gzipInputStream
                    = new GZIPInputStream(new ByteArrayInputStream(decodedData));

            bout = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];

            while( true ) {
                int n = gzipInputStream.read( buf, 0, buf.length );
                if (n == -1) break;
                bout.write(buf,0,n);
            }

            bout.flush();
            gzipInputStream.close();

        } catch (Exception ex) {
            logger.debug(" ICMSStream dataDecompressGZip Error :"+ ex.getMessage() );
            logger.debug(" ICMSStream Error gzipedData:"+ gzipedData );
        }

        return  new String( bout.toByteArray() );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 주어진 개수만큼 zero를 버퍼에 쓴다.
     * @param len Zero의 길이
     */
    public static void writeZero(StringBuffer buffer, int len) {
        for (int i = 0; i < len; i++) {
            buffer.append("0");
        }
    }

    /**
     * 버퍼에 zero를 쓰고 주어진 문자를 마지막에 붙인다.
     *
     * 예를들어, len이 10이고 subject이 12345이면
     * 버퍼에 "0000012345"가 쓰여진다.
     *
     * @param subject 맨 뒤에 붙일 문자
     * @param len 버퍼에 쓰여지는 문자의 개수
     */
    public static void writeLeadingZero(StringBuffer buffer, String subject, int len) {
        writeZero(buffer, len - subject.length());
        buffer.append(subject);
    }
    public static void writeZero(StringBuffer buffer, int value, int len) {
        String subject = String.valueOf(value);
        writeZero(buffer, len - subject.length());
        buffer.append(subject);
    }
    public static String writeZero(int value, int len) {
        StringBuffer buffer = new StringBuffer();
        String subject = String.valueOf(value);
        writeZero(buffer, len - subject.length());
        buffer.append(subject);
        return buffer.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 주어진 개수만큼 스페이스를 버퍼에 쓴다.
     * @param len 스페이스의 개수
     */
    public static void writeSpace(StringBuffer buffer, int len) {
        for (int i = 0; i < len; i++) {
            buffer.append(" ");
        }
    }

    public static void writeLeadingSpace(StringBuffer buffer, String subject, int len) {
        buffer.append(subject);
        writeSpace(buffer, len - subject.length());
    }
    
    public static String writeZeroForRetCode(String s) {
        int length = 8;
        StringBuffer buffer = new StringBuffer();
        buffer.append("S");
        int len = length - s.length()-1;
        for (int i = 0; i < len; i++) {
            buffer.append("0");
        }
        buffer.append(s);
        return buffer.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * <p>현재날짜 가져오기 (YYYY-MM-DD)</p>
     * @return  포맷된 날짜 (YYYY-MM-DD).
     */
    public static String getToday () {
        return getToday("yyyy-MM-dd");
    }

    /** 
     * <p>입력받은 형식에 맞는 현재날짜 및 시간 가져옴.</p>
     *
	 * @param    fmt 날짜형식
     * @return   현재일자
	 * -----------------------------------------
	 * 포맷 패턴                         결과
	 * -----------------------------------------
	 * "yyyy.MM.dd G 'at' hh:mm:ss z" -> 1996.07. 10 AD at 15:08:56 PDT
	 * "EEE, MMM d, ''yy"             -> Wed, July 10, '96
	 * "h:mm a"                       -> 12:08 PM
	 * "hh 'o''clock' a, zzzz"        -> 12 o'clock PM, Pacific Daylight Time
	 * "K:mm a, z"                    -> 0:00 PM, PST
	 * "yyyyy.MMMMM.dd GGG hh:mm aaa" -> 1996. July. 10 AD 12:08 PM
     */
    public static String getToday (String fmt) {
        SimpleDateFormat sfmt = new SimpleDateFormat(fmt);
        return sfmt.format(new Date());
    }

}
