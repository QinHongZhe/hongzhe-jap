package com.fujieid.jap.ids;

import com.fujieid.jap.ids.config.JwtConfig;
import com.fujieid.jap.ids.service.IdsIdentityService;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 */
public class IdsIdentityServiceImpl implements IdsIdentityService {
    /**
     * Get the jwt token encryption key string
     *
     * @param identity User/organization/enterprise identification
     * @return Encryption key string in json format
     */
    @Override
    public String getJwksJson(String identity) {
        return IdsIdentityService.super.getJwksJson(identity);
    }

    /**
     * Get the configuration of jwt token encryption
     *
     * @param identity User/organization/enterprise identification
     * @return Encryption key string in json format
     */
    @Override
    public JwtConfig getJwtConfig(String identity) {
        return IdsIdentityService.super.getJwtConfig(identity);
    }
}
