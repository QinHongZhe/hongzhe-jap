/*
 * Copyright (c) 2020-2040, 北京符节科技有限公司 (support@fujieid.com & https://www.fujieid.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fujieid.jap.oauth2.token;

import cn.hutool.core.io.IORuntimeException;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.context.JapAuthentication;
import com.fujieid.jap.core.context.JapContext;
import com.fujieid.jap.core.exception.JapOauth2Exception;
import com.fujieid.jap.core.exception.OidcException;
import com.fujieid.jap.http.JapHttpRequest;
import com.fujieid.jap.http.adapter.jakarta.JakartaRequestAdapter;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oauth2.Oauth2GrantType;
import com.fujieid.jap.oauth2.Oauth2ResponseType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

/**
 * unit test
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 */
public class AccessTokenHelperTest {

    public JapHttpRequest request;
    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.request = new JakartaRequestAdapter(httpServletRequestMock);
    }

    @Test
    public void getTokenNullOAuthConfig() {
        Assert.assertThrows(JapOauth2Exception.class, () -> AccessTokenHelper.getToken(request, null));
    }

    @Test
    public void getTokenEmptyOAuthConfig() {
        Assert.assertThrows(JapOauth2Exception.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()));
    }

    @Test
    public void getTokenCodeResponseType() {
        Assert.assertThrows(JapOauth2Exception.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setResponseType(Oauth2ResponseType.CODE)));
    }

    @Test
    public void getTokenCodeResponseTypeDoNotCheckState() {
        // Http url must be not blank!
        Assert.assertThrows(IllegalArgumentException.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setVerifyState(false)
            .setResponseType(Oauth2ResponseType.CODE)));
    }

    @Test
    public void getTokenCodeResponseTypeNullCache() {
        JapAuthentication.setContext(new JapContext());
        JapAuthentication.getContext().setCache(null);
        Assert.assertThrows(NullPointerException.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setVerifyState(false)
            .setResponseType(Oauth2ResponseType.CODE)
            .setEnablePkce(true)
            .setCallbackUrl("setCallbackUrl")
            .setTokenUrl("setTokenUrl")));
    }

    @Test
    public void getTokenCodeResponseTypeErrorTokenUrl() {
        // UnknownHostException: setTokenUrl
        JapAuthentication.setContext(new JapContext());
        JapAuthentication.getContext().setCache(new JapLocalCache());
        Assert.assertThrows(JapOauth2Exception.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setVerifyState(false)
            .setResponseType(Oauth2ResponseType.CODE)
            .setEnablePkce(true)
            .setCallbackUrl("setCallbackUrl")
            .setTokenUrl("setTokenUrl")));
    }

    @Test
    public void getTokenTokenResponseType() {
        // Oauth2Strategy failed to get AccessToken.
        Assert.assertThrows(JapOauth2Exception.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setResponseType(Oauth2ResponseType.TOKEN)));
    }

    @Test
    public void getTokenPasswordGrantTypeNullTokenUrl() {
        // Http url must be not blank!
        Assert.assertThrows(IllegalArgumentException.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setGrantType(Oauth2GrantType.PASSWORD)
            .setScopes(new String[]{"read"})));
    }

    @Test
    public void getTokenPasswordGrantTypeErrorTokenUrl() {
        // UnknownHostException: setTokenUrl
        Assert.assertThrows(JapOauth2Exception.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setGrantType(Oauth2GrantType.PASSWORD)
            .setScopes(new String[]{"read"})
            .setTokenUrl("setTokenUrl")));
    }

    @Test
    public void getTokenClientCredentialsGrantTypeErrorTokenUrl() {
        // UnknownHostException: setTokenUrl
        Assert.assertThrows(JapOauth2Exception.class, () -> AccessTokenHelper.getToken(request, new OAuthConfig()
            .setGrantType(Oauth2GrantType.CLIENT_CREDENTIALS)
            .setScopes(new String[]{"read"})
            .setTokenUrl("setTokenUrl")));
    }
}
