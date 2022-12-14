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
package com.fujieid.jap.ids.provider;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.ids.model.IdsScope;
import com.xkcoding.json.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Define and manage the scope used in oauth authorization.
 * Read, write, openid, email, and phone are supported by default.
 * If developers need to modify the scope, they can use the following methods:
 * <p>
 * {@link com.fujieid.jap.ids.provider.IdsScopeProvider#addScope(IdsScope)}
 * <p>
 * Note: Whether it is a custom scope or a system built-in scope, openid must be included, which is a required option for enabling OIDC
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 */
public class IdsScopeProvider {

    private static final List<IdsScope> SCOPES = new ArrayList<>();

    static {
        addScope(new IdsScope().setCode("read").setDescription("Allow users to read protected resources."));
        addScope(new IdsScope().setCode("write").setDescription("Allow users to operate (add, delete, modify) protected resources."));
        addScope(new IdsScope().setCode("openid").setDescription("OpenID connect must include scope."));
        addScope(new IdsScope().setCode("profile").setDescription("Allow access to user's basic information."));
        addScope(new IdsScope().setCode("email").setDescription("Allow access to user's mailbox."));
        addScope(new IdsScope().setCode("phone").setDescription("Allow access to the user’s phone number."));
        addScope(new IdsScope().setCode("address").setDescription("Allow access to the user's address."));
    }

    /**
     * Add a single scope.
     * Note: This method is to add data to the existing scope collection.
     * <p>
     * If the scope to be added already exists, the new scope will overwrite the original scope
     *
     * @param idsScope single scope
     */
    public static void addScope(IdsScope idsScope) {
        if (null == idsScope || StringUtil.isEmpty(idsScope.getCode())) {
            return;
        }
        long num = SCOPES.stream().filter(scope -> scope.getCode().equals(idsScope.getCode())).count();
        if (num > 0) {
            SCOPES.stream()
                .filter(scope -> scope.getCode().equals(idsScope.getCode()))
                .findFirst()
                .map(scope -> scope.setDescription(idsScope.getDescription()));
            return;
        }
        SCOPES.add(idsScope);
    }

    /**
     * Return the set of available scopes after deduplication according to the scope code
     *
     * @return Unique scope collection
     */
    public static List<IdsScope> getScopes() {
        return SCOPES.stream().collect(Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(
                Comparator.comparing(
                    IdsScope::getCode))), ArrayList::new));
    }

    /**
     * Obtain the scope collection through scope code (does not contain duplicates)
     *
     * @param codes scope code
     * @return Unique scope collection
     */
    public static List<IdsScope> getScopeByCodes(Collection<String> codes) {
        if (ObjectUtil.isEmpty(codes)) {
            return new ArrayList<>(0);
        }
        return Optional.ofNullable(SCOPES.stream().filter((scope) -> codes.contains(scope.getCode()))
            .collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(
                    Comparator.comparing(
                        IdsScope::getCode))), ArrayList::new)))
            .orElse(new ArrayList<>(0));
    }

    /**
     * Get the code of all scopes
     *
     * @return code of all scopes
     */
    public static List<String> getScopeCodes() {
        return SCOPES.stream().map(IdsScope::getCode).distinct().collect(Collectors.toList());
    }

}
