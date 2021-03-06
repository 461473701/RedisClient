package zx.util;

import com.datalook.gain.jedis.command.common.*;
import com.datalook.gain.jedis.command.executor.CommandExecutor;
import com.datalook.gain.jedis.command.executor.CommandMultiExecutor;
import com.datalook.gain.jedis.command.executor.Executor;
import com.datalook.gain.jedis.command.hash.*;
import com.datalook.gain.jedis.command.set.CommandGet;
import com.datalook.gain.jedis.command.set.CommandMGet;
import com.datalook.gain.jedis.command.set.CommandSet;
import com.datalook.gain.jedis.result.JedisResult;
import com.datalook.gain.util.ValidateUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import redis.clients.jedis.Response;
import redis.clients.util.SafeEncoder;
import zx.codec.DefaultDecode;
import zx.constant.Constant;
import zx.design.Main;
import zx.jedis.JedisFactory;
import zx.model.TableData;
import zx.redis.RedisType;
import zx.redis.command.ExecutorProxy;
import zx.redis.command.config.Config_Get;

import java.util.*;

/**
 * 功能描述：
 * 时间：2016/3/29 11:23
 *
 * @author ：zhaokuiqiang
 */
public class JedisUtil {

    /**
     * 存储当前db的key和field
     */
    public final static Map<String,ObservableList<String>> CURRENTKEYFIELDS = new HashMap<>();

    /**
     * 获取所有key
     * @param id
     * @param index 数据库index
     * @return
     */
    public static HashSet<String> getAllKey(String id,int index){
        Executor executor = getExecutor(id,false);
        JedisResult jedisResult = executor.addCommand(new CommandSelect(index)).execute().getResult();
        if(!Constant.REDIS_OK.equals(jedisResult.getResult().toString())){
            return new HashSet<>();
        }
        // hgetall所有值 keys * mget
        jedisResult = executor.addCommands(new CommandKeys("*")).execute().getResult();
        return (HashSet<String>) jedisResult.getResult();
    }

    /**
     * 获取所有类型是string的value
     * @return
     */
    /*public static List<TableData> getAllKeyValue(String id, int index){
        List<TableData> result = new ArrayList<>();
        HashSet<String> set = getAllKey(id,index);
        if(set.size() > 0){
            Iterator<String> iterator = set.iterator();
            List<TableData> hashKeys = new ArrayList<>();
            List<String> stringKeys = new ArrayList<>();
            while(iterator.hasNext()){
                String key = iterator.next();
                String type = getKeyType(getExecutor(id,false),key);
                if(Constant.REDIS_HASH.equals(type)){
                    hashKeys.add(new TableData(key));
                }else if(Constant.REDIS_STRING.equals(type)){
                    stringKeys.add(key);
                }
            }
            //遍历string
            List<String> stringValues = getAllString(id,stringKeys.toArray(new String[]{}));
            for(int i = 0; i < stringKeys.size(); i++) {
                if(stringValues.get(i) != null){
                    TableData tableData = new TableData();
                    tableData.setKey(stringKeys.get(i));
                    StringBuilder sb = new StringBuilder();
                    tableData.setValue(stringValues.get(i));
                    tableData.setType(RedisType.STRING);
                    tableData.setSource(stringKeys.get(i));
                    result.add(tableData);
                }
            }
            //获取hash
            List<List<TableData>> hashFields = getAllHash(id,hashKeys);
            if(hashFields != null && hashFields.size() > 0){
                //清空缓存中存储的当前hash数据
                CURRENTKEYFIELDS.clear();
            }
            //遍历hash
            for(int i = 0; i < hashFields.size(); i++) {
                TableData tableData = new TableData();
                tableData.setKey(hashKeys.get(i).getKey());
                tableData.setType(RedisType.HASH);
                List<TableData> hashKeyList = hashFields.get(i);
                StringBuilder sb = new StringBuilder();
//                List<RedisProto.User> sourceData = new ArrayList<>();
                ObservableList<String> observableList = FXCollections.observableArrayList();
                for(int j = 0; j < hashKeyList.size(); j++){
                    TableData data = hashKeyList.get(i);
                    String decodeFields = data.getField();
                    sb.append(decodeFields);
                    tableData.setField(sb.toString());
                    if(j != 0){
                        sb.append(" & ");
                    }
                    observableList.add(decodeFields);
//                    sourceData.add(user);
//                    tableData.setSource(sourceData);
                    //不显示value
                    *//*sb.append("value:");
                    try {
                        RedisProto.User user = CODEC.decode(maps.get(hashFiels));
                        sb.append("{").append(TextFormat.printToUnicodeString(user).replace("\n","")).append("}");
                        tableData.setSource(user);
                    } catch(InvalidProtocolBufferException e) {
                        sb.append(DefaultDecode.DEFAULT.decode(maps.get(hashFiels)));
                        tableData.setSource(DefaultDecode.DEFAULT.decode(maps.get(hashFiels)));
                    }*//*
                }
                CURRENTKEYFIELDS.put(tableData.getKey(),observableList);
                tableData.setValue(sb.toString());
                result.add(tableData);
            }
        }
        return result;
    }*/

    /**
     * 获取所有类型是string的value
     * @return
     */
    public static List<String> getAllString(String id,String [] keys){
        if(keys == null || keys.length == 0){
            return new ArrayList<>();
        }
        Executor executor = getExecutor(id,false);
        JedisResult jedisResult = executor.addCommands(new CommandMGet(keys)).execute().getResult();
        return (List<String>) jedisResult.getResult();
    }

    /**
     * 获取所有类型是string的value
     * @return
     */
    public static List<TableData> getAllHash(String id, TableData tableData){
        List<TableData> rsList = new ArrayList<>();
        if(tableData == null || tableData.getKey() == null){
            return rsList;
        }
        Executor executor = getExecutor(id,true);
        // 改为获取hash的field
//            executor.addCommands(new CommandHashGetAll(keys.get(i)));
        executor.addCommands(new CommandHashKeys(tableData.getKey()));
        JedisResult jedisResult = executor.execute().getResult();
        Response<List<Set<byte[]>>> result = (Response<List<Set<byte[]>>>) jedisResult.getResult();

        /*List<Set<byte[]>> setList = result.get();
        for(int i = 0; i < setList.size(); i++) {
            Iterator<byte[]> iterator = setList.get(i).iterator();
            while(iterator.hasNext()){
                String field = DefaultDecode.DEFAULT.decode(iterator.next());
                TableData data = new TableData();
                data.setKey(tableData.getKey());
                data.setField(field);
                data.setType(tableData.getType());
                rsList.add(data);
            }
        }*/
        List<TableData> tableDatas = CodecUtil.decode(tableData,result.get());
        CodecUtil.decode((Object) tableDatas);
        return tableDatas;
    }

    /**
     * 获取当前库key的类型
     * @return
     */
    public static RedisType getKeyType(String id, String key){
        return RedisType.valueOf(getKeyType(getExecutor(id,false),key).toUpperCase());
    }

    /**
     * 获取当前库key的类型
     * @return
     */
    public static String getKeyType(Executor executor,String key){
        JedisResult jedisResult = executor.addCommand(new CommandType(key)).execute().getResult();
        return String.valueOf(jedisResult.getResult());
    }

    public static String getHashValue(String id,String key,String field){
        Executor executor = getExecutor(id,false);
        JedisResult jedisResult = executor.addCommand(new CommandHashGet(key,field)).execute().getResult();
        if(jedisResult.getResult() != null){
            return CodecUtil.decode(jedisResult.getResult());
        }else{
            return null;
        }
    }

    /**
     * 执行get
     * @param id
     * @param tableData
     * @return
     */
    public static TableData getValueSet(String id,TableData tableData){
        if(ValidateUtils.isEmpty(id) || ValidateUtils.isEmpty(tableData.getKey())){
            tableData.setValue("查询失败key:" + String.valueOf(tableData.getKey()));
            throw new NullPointerException("查询失败无效的key");
        }
        Executor executor = getExecutor(id,false);
        JedisResult jedisResult;
        jedisResult = executor.addCommand(new CommandGet(tableData.getKey())).execute().getResult();
        tableData.setValue(handlerJedisResult(jedisResult));
        if(jedisResult.getResult() == null){
        }else{
            tableData.setSource(SafeEncoder.encode((byte[]) jedisResult.getResult()));
        }
        return tableData;
    }

    /**
     * 执行hget
     * @param id
     * @param tableData
     * @return
     */
    public static TableData getValueHash(String id,TableData tableData){
        if(ValidateUtils.isEmpty(id) || ValidateUtils.isEmpty(tableData.getKey()) || ValidateUtils.isEmpty(tableData.getField())){
            tableData.setValue("操作失败，key："+ String.valueOf(tableData.getKey()) + " field:"+String.valueOf(tableData.getField()));
            throw new NullPointerException(tableData.getValue());
        }
        Executor executor = getExecutor(id,false);
        JedisResult jedisResult;
        jedisResult = executor.addCommand(new CommandHashGet(tableData.getKey(),tableData.getField())).execute().getResult();
        tableData.setValue(handlerJedisResult(jedisResult));
        if(tableData.getValue() == null){
            tableData.setValue("无效的key");
        }
        return tableData;
    }

    public static String handlerJedisResult(JedisResult jedisResult){
        if(jedisResult.getResult() != null){
            return CodecUtil.decode(jedisResult.getResult());
        }else{
            return null;
        }
    }

    /**
     * 保存string
     * @param key
     * @param value
     */
    public static boolean saveData(String key,String value){
        if(ValidateUtils.isEmpty(Main.redisDB.getId())){
            return false;
        }
        if(ValidateUtils.isEmpty(key) || ValidateUtils.isEmpty(value)){
            return false;
        }
        Executor executor = getExecutor(Main.redisDB.getId(),false);
        try {
            executor.addCommand(new CommandSet(key,value)).execute();
        }catch(RuntimeException e){
            return false;
        }
        return true;
    }

    /**
     * 保存hash
     * @param key
     * @param field
     * @param value
     */
    public static boolean saveData(String key,String field,String value){
        if(ValidateUtils.isEmpty(Main.redisDB.getId())){
            return false;
        }
        if(ValidateUtils.isEmpty(key) || ValidateUtils.isEmpty(field)
                 || ValidateUtils.isEmpty(value)){
            return false;
        }
        Executor executor = getExecutor(Main.redisDB.getId(),false);
        try {
            executor.addCommand(new CommandHashSet(key,field,value)).execute();
        }catch(RuntimeException e){
            return false;
        }
        return true;
    }

    /**
     * 选择数据库
     * @param index
     * @return
     */
    public static boolean selectDB(int index){
        try {
            getExecutor(Main.redisDB.getId(),false).addCommand(new CommandSelect(index)).execute();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取数据库数量
     * @return
     */
    public static String getDatabases(String id){
        Executor executor = new CommandExecutor(JedisFactory.getJedis(id));
        JedisResult<ArrayList> jedisResult = executor.addCommand(new CommandConfigGet(Config_Get.DATABASES)).execute().getResult();
        return String.valueOf(jedisResult.getResult().get(1));
    }

    public static void destroyAllRedis(){
        JedisFactory.destroyAllRedis();
    }

    public static void destroyRedis(String id){
        JedisFactory.destroyRedis(id);
    }

    public static Executor getExecutor(String id,boolean isMulti){
//        if(Main.redisDB.getIndex() != null)
//            executor.addCommand(new CommandSelect(Main.redisDB.getIndex()));
        return new ExecutorProxy(id,isMulti);
    }

}
