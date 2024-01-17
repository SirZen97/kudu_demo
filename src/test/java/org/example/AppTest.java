package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.kudu.shaded.io.micrometer.core.instrument.util.StringUtils;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.*;
import java.util.Random;
import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        String src  = "你好";
        System.out.println(subStringInBytes(src,Charset.forName("UTF-8"),0,3)+" "+getBytesLength("", StandardCharsets.UTF_8));
    }

    private String subStringInBytes(String src, Charset charset,int startIdx,int bytesLength){
        if(src==null||src.isEmpty()||bytesLength<=0||startIdx>=src.length()){
            return null;
        }
        //总编码字节数,当encodeBytes首次大于形参bytesLength时,表明以截取到足够长的子串，本方法返回结果
        int encodeBytes = 0;
        //接收每个char字符编码的结果,仅有Underflow这种状态才表明编码成功
        CoderResult coderResult;
        //接收返回的子串
        StringBuilder encodeResultBuilder = new StringBuilder();
        try {
            //字符编码器,用于将char转换成byte
            CharsetEncoder charsetEncoder = charset.newEncoder();
            //字符缓存,用于存储待编码字符
            CharBuffer charBuffer = CharBuffer.allocate(1);
            //字节缓存,用于存储charBuffer转换后的字节编码
            ByteBuffer encodeBuffer = ByteBuffer.allocate(8);
            //开始将src中指定范围的每一个字符转换成字节
            for (int i = startIdx; i < src.length(); i++) {
                charBuffer.put(src.charAt(i));
                charBuffer.flip(); //切换成读模式
                coderResult = charsetEncoder.encode(charBuffer, encodeBuffer, true);
                if(coderResult.isUnderflow()){
                    encodeBytes += encodeBuffer.position(); //position表示当前字符对应多少个字节
                    if(encodeBytes<=bytesLength){
                        encodeResultBuilder.append(src.charAt(i));
                    }else{
                        break;
                    }
                }else{
                    break;
                }
                charBuffer.flip(); //切换成写模式
                //清空缓存,便于下一次迭代
                charBuffer.clear();
                encodeBuffer.clear();
            }
        }catch (BufferOverflowException e){
            e.printStackTrace();
        }catch (ReadOnlyBufferException e){
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (CoderMalfunctionError e){
            e.printStackTrace();
        }
        return encodeResultBuilder.length()>0?encodeResultBuilder.toString():null;
    }

    private int getBytesLength(String src,Charset charset){
        if(src==null){
            return 0;
        }
        return src.getBytes(charset).length;
    }

}
