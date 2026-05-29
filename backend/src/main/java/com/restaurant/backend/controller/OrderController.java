package com.restaurant.backend.controller;

import com.restaurant.backend.entity.Order;
import com.restaurant.backend.entity.OrderItem;
import com.restaurant.backend.entity.User;
import com.restaurant.backend.repository.MenuItemRepository;
import com.restaurant.backend.repository.OrderRepository;
import com.restaurant.backend.repository.UserRepository;
import com.restaurant.backend.service.TelegramService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final TelegramService telegramService;

    @GetMapping("/test-bot")
    public String testBot() {
        telegramService.sendOrderNotification("✅ Test xabari: Bot tizimi muvaffaqiyatli ulandi!", "DINING");
        telegramService.sendOrderNotification("🚀 Test xabari: Dastavka boti muvaffaqiyatli ulandi!", "DELIVERY");
        return "Test xabarlari botlarga yuborildi. Telegramni tekshiring!";
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User must be logged in to place an order.");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setOrderType(request.getOrderType() != null ? request.getOrderType() : "DINING");
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "CASH");
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setDeliveryTime(request.getDeliveryTime() != null ? request.getDeliveryTime() : "ASAP");
        order.setLatitude(request.getLatitude());
        order.setLongitude(request.getLongitude());

        List<OrderItem> items = request.getItems().stream().map(itemReq -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenuItem(menuItemRepository.findById(itemReq.getMenuItemId()).orElseThrow());
            item.setQuantity(itemReq.getQuantity());
            item.setPriceAtTime(item.getMenuItem().getPrice());
            return item;
        }).toList();

        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPriceAtTime().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
        
        if (total.compareTo(new BigDecimal("500000")) > 0) {
            order.setDiscountAmount(total.multiply(new BigDecimal("0.05")));
        } else {
            order.setDiscountAmount(BigDecimal.ZERO);
        }
        
        order.setFinalAmount(order.getTotalAmount().subtract(order.getDiscountAmount()));

        Order savedOrder = orderRepository.save(order);

        try {
            sendTelegramNotificationToAdmin(savedOrder);
        } catch (Exception e) {
            System.err.println("Telegram notification error: " + e.getMessage());
        }

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        Order order = orderRepository.findById(id).orElseThrow();
        String oldStatus = order.getStatus();
        order.setStatus(request.getStatus());
        orderRepository.save(order);
        
        if (!oldStatus.equals(request.getStatus())) {
            try {
                notifyUserAboutStatusChange(order);
            } catch (Exception e) {
                System.err.println("Status notification error: " + e.getMessage());
            }
        }
        
        return ResponseEntity.ok("Order status updated to " + request.getStatus());
    }

    private void notifyUserAboutStatusChange(Order order) {
        User user = order.getUser();
        if (user.getTelegramChatId() != null && !user.getTelegramChatId().trim().isEmpty()) {
            String statusEmoji = switch (order.getStatus()) {
                case "PREPARING" -> "👨‍🍳";
                case "SHIPPING" -> "🚚";
                case "COMPLETED" -> "✅";
                case "CANCELLED" -> "❌";
                default -> "ℹ️";
            };
            
            String message = String.format(
                "<b>%s Buyurtma holati yangilandi!</b>\n\n" +
                "Buyurtma ID: #%d\n" +
                "Holati: <b>%s</b>",
                statusEmoji, order.getId(), order.getStatus()
            );
            
            telegramService.sendToChat(user.getTelegramChatId(), message, order.getOrderType());
        }
    }

    private void sendTelegramNotificationToAdmin(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>🔔 YANGI BUYURTMA #").append(order.getId()).append("</b>\n");
        sb.append("<b>----------------------------</b>\n");
        sb.append("<b>Turi:</b> ").append("DELIVERY".equals(order.getOrderType()) ? "🚗 Dastavka" : "🍽 Restaran").append("\n");
        sb.append("<b>Mijoz:</b> ").append(order.getUser().getName()).append("\n");
        
        String phone = order.getUser().getPhoneNumber();
        sb.append("<b>Tel:</b> ").append(phone != null ? phone : order.getUser().getEmail()).append("\n");
        sb.append("<b>To'lov:</b> ").append(order.getPaymentMethod()).append("\n\n");
        
        sb.append("<b>Taomlar:</b>\n");
        for (OrderItem item : order.getItems()) {
            sb.append("• ").append(item.getMenuItem().getName())
              .append(" x ").append(item.getQuantity()).append("\n");
        }
        
        sb.append("\n<b>Jami:</b> ").append(order.getFinalAmount()).append(" so'm\n");
        
        if ("DELIVERY".equals(order.getOrderType())) {
            sb.append("\n<b>Manzil:</b> ").append(order.getDeliveryAddress()).append("\n");
            if (order.getLatitude() != null) {
                sb.append("📍 <b>Lokatsiya:</b> https://www.google.com/maps?q=").append(order.getLatitude()).append(",").append(order.getLongitude());
            }
        }

        telegramService.sendOrderNotification(sb.toString(), order.getOrderType());
    }

    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(orderRepository.findByUserId(user.getId()));
    }
}

@Data
class OrderRequest {
    private List<OrderItemRequest> items;
    private String orderType;
    private String paymentMethod;
    private String deliveryTime;
    private String deliveryAddress;
    private Double latitude;
    private Double longitude;

    @Data
    public static class OrderItemRequest {
        private Long menuItemId;
        private Integer quantity;
    }
}

@Data
class StatusRequest {
    private String status;
}
