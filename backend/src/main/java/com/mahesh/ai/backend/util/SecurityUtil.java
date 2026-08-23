package com.mahesh.ai.backend.util;

import com.mahesh.ai.backend.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        UserPrincipal principal = getCurrentUser();
        return principal != null ? principal.getId() : null;
    }

    public static String getCurrentUserEmail() {
        UserPrincipal principal = getCurrentUser();
        return principal != null ? principal.getUsername() : null;
    }

    public static String getCurrentUserRole() {
        UserPrincipal principal = getCurrentUser();
        if (principal != null && !principal.getAuthorities().isEmpty()) {
            return principal.getAuthorities().iterator().next().getAuthority();
        }
        return null;
    }

    public static boolean isCurrentUserAdmin() {
        String role = getCurrentUserRole();
        return "ROLE_ADMIN".equals(role);
    }
}
