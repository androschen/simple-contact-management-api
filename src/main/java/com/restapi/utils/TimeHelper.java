package com.restapi.utils;

public class TimeHelper {
   public static Long next30Days() {
      return System.currentTimeMillis() + (1000 * 60 * 24 * 30);
   }
}
