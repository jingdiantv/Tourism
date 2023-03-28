package com.whiner.network.base;

public class CacheModel {

    public enum Type {
        ONLY_CACHE, FIRST_CACHE, NO_CACHE
    }

    private Type type = Type.NO_CACHE;
    private String key;
    private long keep_time;

    public CacheModel() {

    }

    public CacheModel(Type type, String key, long keep_time) {
        this.type = type;
        this.key = key;
        this.keep_time = keep_time;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getKeep_time() {
        return keep_time;
    }

    public void setKeep_time(long keep_time) {
        this.keep_time = keep_time;
    }

}
