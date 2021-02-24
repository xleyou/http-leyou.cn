package com.leyou.auth.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient /*extends UserApi*/{
}
