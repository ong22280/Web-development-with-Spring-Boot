        // Sittipong Hemloun 6410401183
package ku.cs.kafe.service;

import ku.cs.kafe.entity.Member;
import ku.cs.kafe.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private MemberRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        // SimpleGrantedAuthority คือ คลาสที่เก็บข้อมูลเกี่ยวกับสิทธิ์การเข้าถึงของผู้ใช้งาน เช่น สิทธิ์ในการเข้าถึงหน้าเว็บไซต์ สิทธิ์ในการใช้งาน API ต่างๆ
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }
}
