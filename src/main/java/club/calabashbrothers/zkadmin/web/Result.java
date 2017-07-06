package club.calabashbrothers.zkadmin.web;


import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaojiacan on 2/19/16.
 */
public class Result {
    public static final Map<String, Object> SIMPLE_SUCCESS = new HashMap<String, Object>();
    public static final Map<String, Object> SIMPLIE_FAIL = new HashMap<String, Object>();


    public static final Map<String, Object> NONE_ZK_CONNECTION = new HashMap<String, Object>();

    static {
        SIMPLE_SUCCESS.put("code",200);
        SIMPLE_SUCCESS.put("info","success");

        SIMPLIE_FAIL.put("code", -200);
        SIMPLIE_FAIL.put("info", "fail");

        NONE_ZK_CONNECTION.put("code",-200);
        NONE_ZK_CONNECTION.put("info","This Operation need connecting zookeeper first");
    }


    public static Map<String, Object> successWrapper(Object embed){
        return successWrapper("object",embed);
    }
    public static Map<String, Object> successWrapper(String name,Object embed){
        Map resultMap = new HashMap();
        resultMap.put("code",200);
        resultMap.put("info","success");
        resultMap.put(name, embed);
        return resultMap;
    }

    public static Map<String, Object> successWrapper(Page page){

        Map resultMap = new HashMap();
        resultMap.put("code",200);
        resultMap.put("info","success");
        Map<String, Object> paginInfo = new HashMap<String, Object>();
        if( page != null && page.size() > 0 ) {
            long totalNum =page.getTotal();
            long totalPage =page.getPages();
            paginInfo.put("count", totalNum );
            paginInfo.put( "page", page.getPageNum());
            paginInfo.put( "pageCount", totalPage );
            paginInfo.put("list",page);
        } else {
            paginInfo.put( "count", 0 );
            paginInfo.put( "page", 0 );
            paginInfo.put( "pageCount", 0 );
        }
        resultMap.put("object", paginInfo);

        return resultMap;
    }


    public static Map<String, Object> normalWrapper(StatusCode statusCode,String info){
        Map resultMap = new HashMap();
        resultMap.put("code",statusCode.getCode());
        resultMap.put("info",info);
        return resultMap;
    }
    public static Map<String, Object> normalWrapper(Integer statusCode,String info){
        Map resultMap = new HashMap();
        resultMap.put("code",statusCode);
        resultMap.put("info",info);
        return resultMap;
    }

    public static Map<String, Object> errorWrapper(String info){
        Map resultMap = new HashMap();
        resultMap.put("code",StatusCode.FAIL.getCode());
        resultMap.put("info",info);
        return resultMap;
    }

    /**
     * 表单错误返回的格式
     * @param formKey
     * @param info
     * @return
     */
    public static Map<String, Object> formErrorWrapper(String  formKey,String info){
        Map resultMap = new HashMap();
        resultMap.put("code",StatusCode.FAIL.getCode());
        resultMap.put("info",info);
        resultMap.put("formKey",formKey);
        return resultMap;
    }
}
