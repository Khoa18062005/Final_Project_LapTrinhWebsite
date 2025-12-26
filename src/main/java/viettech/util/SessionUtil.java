package viettech.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    /**
     * Lấy hoặc tạo session
     */
    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    /**
     * Lấy session hiện tại (không tạo mới)
     */
    public static HttpSession getCurrentSession(HttpServletRequest request) {
        return request.getSession(false);
    }

    /**
     * Set attribute vào session
     */
    public static void setAttribute(HttpServletRequest request, String name, Object value) {
        HttpSession session = request.getSession();
        session.setAttribute(name, value);
    }

    /**
     * Lấy attribute từ session
     */
    public static Object getAttribute(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        return (session != null) ? session.getAttribute(name) : null;
    }

    /**
     * Lấy attribute với type casting
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request, String name, Class<T> type) {
        Object value = getAttribute(request, name);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Xóa attribute khỏi session
     */
    public static void removeAttribute(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(name);
        }
    }

    /**
     * Set success message
     */
    public static void setSuccessMessage(HttpServletRequest request, String message) {
        setAttribute(request, "successMessage", message);
    }

    /**
     * Set error message
     */
    public static void setErrorMessage(HttpServletRequest request, String message) {
        setAttribute(request, "errorMessage", message);
    }

    /**
     * Set info message
     */
    public static void setInfoMessage(HttpServletRequest request, String message) {
        setAttribute(request, "infoMessage", message);
    }

    /**
     * Invalidate session (logout)
     */
    public static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    public static boolean isAuthenticated(HttpServletRequest request) {
        return getAttribute(request, "auth") != null;
    }

    /**
     * Lấy user hiện tại
     */
    public static <T> T getCurrentUser(HttpServletRequest request, Class<T> type) {
        return getAttribute(request, "auth", type);
    }

    private SessionUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }
}