package viettech.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CookieUtil {

    /**
     * Tạo cookie với giá trị đã encode
     */
    public static void addCookie(HttpServletResponse response, 
                                 String name, 
                                 String value, 
                                 int maxAge) {
        try {
            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
            Cookie cookie = new Cookie(name, encodedValue);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
            cookie.setHttpOnly(true); // Bảo mật
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Đọc cookie và decode giá trị
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Lấy cookie object
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * Xóa cookie
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * Xóa nhiều cookies
     */
    public static void deleteCookies(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     String... names) {
        for (String name : names) {
            deleteCookie(response, name);
        }
    }

    /**
     * Xóa tất cả cookies
     */
    public static void deleteAllCookies(HttpServletRequest request, 
                                       HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

    private CookieUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }
}