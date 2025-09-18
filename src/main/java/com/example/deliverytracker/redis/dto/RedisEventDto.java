package com.example.deliverytracker.redis.dto;

import lombok.Getter;

@Getter
public class RedisEventDto<T> {
    private EventType type;
    private T data;

    public RedisEventDto(EventType type, T data) {
        this.type = type;
        this.data = data;
    }
}