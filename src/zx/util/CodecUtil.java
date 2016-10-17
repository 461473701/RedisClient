package zx.util;

import com.datalook.gain.jedis.result.JedisResult;
import com.google.protobuf.InvalidProtocolBufferException;
import redis.clients.jedis.Response;
import zx.codec.Codec;
import zx.codec.CodecEnum;
import zx.codec.DefaultDecode;
import zx.model.TableData;

import java.util.*;

/**
 * 解码工具类
 * 时间：2016-10-13
 *
 * @author: zhaokuiqiang
 */
public class CodecUtil {

    public static String decode(Object source){
        if(source == null){
            return "null";
        }
        if(source instanceof String){
            return source.toString();
        }else if(source instanceof Response){
            return decode(decode((Response<List<Set<byte[]>>>) source));
        }else if(source instanceof Object[]){
            Object[] s = (Object[]) source;
            return decode(decode((TableData) s[0],(List<Set<byte[]>>) s[1]));
        }else{
            return decode((byte[])source);
        }
    }

    public static List<String> decode(Response<List<Set<byte[]>>> response){
        List<String> resultList = new ArrayList<>();
        List<Set<byte[]>> list = response.get();
        for(int i = 0; i < list.size(); i++) {
            Iterator<byte[]> iterator = list.get(i).iterator();
            while(iterator.hasNext()){
                String temp = CodecUtil.decode(iterator.next());
                resultList.add(temp);
            }
        }
        return resultList;
    }

    public static List<TableData> decode(TableData tableData,List<Set<byte[]>> setList){
        List<TableData> resultList = new ArrayList<>();
        for(int i = 0; i < setList.size(); i++) {
            Iterator<byte[]> iterator = setList.get(i).iterator();
            while(iterator.hasNext()){
                String field = DefaultDecode.DEFAULT.decode(iterator.next());
                TableData data = new TableData();
                data.setField(field);
                data.setKey(tableData.getKey());
                data.setType(tableData.getType());
                resultList.add(data);
            }
        }
        return resultList;
    }

    /**
     * 尝试解码
     * @param source
     * @return
     */
    public static String decode(byte [] source){
        CodecEnum [] codecs = CodecEnum.values();
        String result = null;
        for(CodecEnum codec : codecs) {
            try {
                result = codec.decode(source);
                break;
            } catch(InvalidProtocolBufferException e) {
            } catch(RuntimeException e){
                e.printStackTrace();
                result = "解码失败,解码类型："+codec.name();
                break;
            }
        }
        //未解码，使用默认解码方式解码
        if(result == null){
            result = DefaultDecode.DEFAULT.decode(source);
        }
        return result;
    }

    public static String decode(List lists){
        StringBuilder stringBuilder = new StringBuilder();
        for(int j = 0; j < lists.size(); j++) {
            Object obj = lists.get(j);
            stringBuilder.append(obj.toString());
        }
        return stringBuilder.toString();
    }

    /**
     * 使用指定方式编码
     * @param source
     * @param codec
     * @return
     */
    public static byte[] encode(Object source,Codec codec) {
        return codec.encode(source);
    }

}
