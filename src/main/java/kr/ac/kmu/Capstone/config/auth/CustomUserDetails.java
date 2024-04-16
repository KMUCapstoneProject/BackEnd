package kr.ac.kmu.Capstone.config.auth;

import kr.ac.kmu.Capstone.entity.Role;
import kr.ac.kmu.Capstone.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails/*, OAuth2User*/ {
    private User user;
    private Map<String, Object> attribute;

    /* 일반 로그인 생성자 */
    public CustomUserDetails(User user) {
        this.user = user;
    }

/*    *//* OAuth2 로그인 사용자 *//*
    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attribute = attributes;
    }*/

    /* 유저의 권한 목록, 권한 반환*/
/*    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return "ROLE_" + user.getRoleKey();
            }
        });
        return collect;
    }*/


    private GrantedAuthority getAuthority(Role role) {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        switch (user.getRoles()) {
            case ADMIN : authorityList.add(getAuthority(Role.ADMIN));
            case MANAGER : authorityList.add(getAuthority(Role.MANAGER));
            case USER : authorityList.add(getAuthority(Role.USER));
        }

        return authorityList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    //	public String getNickname() {
//		return user.getNickname();
//	}
    /* 계정 만료 여부
     * true :  만료 안됨
     * false : 만료
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /* 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /* 비밀번호 만료 여부
     * true : 만료 안 됨
     * false : 만료
     */

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /* 사용자 활성화 여부
     * true : 활성화 됨
     * false : 활성화 안 됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /* OAuth2User 타입 오버라이딩 */
/*    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }*/
}