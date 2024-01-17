package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.kudu.shaded.io.micrometer.core.instrument.util.StringUtils;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class StringTest extends TestCase {
    private String subStringInBytes(String src, Charset charset,int startIdx,int bytesLength){
        if(StringUtils.isEmpty(src)||bytesLength<0||startIdx>=src.length()){
            return null;
        }
        int encodeBytes = 0;
        CoderResult coderResult;
        StringBuilder encodeResultBuilder = new StringBuilder();
        try {
            CharsetEncoder charsetEncoder = charset.newEncoder();
            CharBuffer charBuffer = CharBuffer.allocate(1);
            ByteBuffer encodeBuffer = ByteBuffer.allocate(8);
            for (int i = startIdx; i < src.length(); i++) {
                charBuffer.put(src.charAt(i));
                charBuffer.flip();
                coderResult = charsetEncoder.encode(charBuffer, encodeBuffer, false);
                if(coderResult.isUnderflow()){
                    encodeBytes += encodeBuffer.position();
                    if(encodeBytes<=bytesLength){
                        encodeResultBuilder.append(src.charAt(i));
                    }else{
                        break;
                    }
                }else{
                    break;
                }
                charBuffer.flip();
                charBuffer.clear();
                encodeBuffer.clear();
            }
        }catch (BufferOverflowException e){
            e.printStackTrace();
        }catch (ReadOnlyBufferException e){
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return encodeResultBuilder.length()>0?encodeResultBuilder.toString():null;
    }
   public StringTest( String testName )
{
    super( testName );
}

    public static Test suite()
    {
        return new TestSuite( StringTest.class );
    }

    public String testApp(){
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder charsetEncoder = charset.newEncoder();
        CharBuffer charBuffer = CharBuffer.allocate(1);
        ByteBuffer encodeByte = ByteBuffer.allocate(8);
        while (true){
            charBuffer.put("哈");
            charsetEncoder.encode(charBuffer,encodeByte,false);
            System.out.println(encodeByte.position()-1);
            charBuffer.clear();
        }
    }
}

