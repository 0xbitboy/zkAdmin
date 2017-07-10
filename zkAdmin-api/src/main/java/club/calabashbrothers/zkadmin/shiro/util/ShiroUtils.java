package club.calabashbrothers.zkadmin.shiro.util;

import club.calabashbrothers.zkadmin.shiro.bind.ShiroUser;
import org.apache.shiro.SecurityUtils;

/**
 * Created by liaojiacan on 2017/5/29.
 */
public class ShiroUtils {

   public static ShiroUser getCurrUser(){
       return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
   }
}
