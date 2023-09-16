package ku.cs.kafe.service;

import ku.cs.kafe.common.Status;
import ku.cs.kafe.entity.Menu;
import ku.cs.kafe.entity.OrderItem;
import ku.cs.kafe.entity.OrderItemKey;
import ku.cs.kafe.entity.PurchaseOrder;
import ku.cs.kafe.model.AddCartRequest;
import ku.cs.kafe.repository.MenuRepository;
import ku.cs.kafe.repository.OrderItemRepository;
import ku.cs.kafe.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private PurchaseOrderRepository orderRepository;

    @Autowired
    private OrderItemRepository itemRepository;

    @Autowired
    private MenuRepository menuRepository;

    private UUID currentOrderId; // ใช้เก็บค่า id ของ order ปัจจุบัน เพื่อให้สามารถเพิ่มเมนูลงใน order เดิมได้

    public PurchaseOrder getCurrentOrder() {
        if (currentOrderId == null)
            createNewOrder();
        // ดึง order ปัจจุบันมา
        return orderRepository.findById(currentOrderId).get();
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public PurchaseOrder getById(UUID id) {
        return orderRepository.findById(id).get();
    }

    // ส่ง order ไปยังฐานข้อมูล
    public void submitOrder() {
        PurchaseOrder currentOrder = orderRepository.findById(currentOrderId).get();
        // ตั้งค่า timestamp และ status ให้กับ order
        currentOrder.setTimestamp(LocalDateTime.now());
        currentOrder.setStatus(Status.CONFIRM);
        orderRepository.save(currentOrder);
        // ล้างค่า currentOrderId เพื่อให้สามารถสร้าง order ใหม่ได้
        currentOrderId = null;
    }

    public void finishOrder(UUID orderId) {
        PurchaseOrder record = orderRepository.findById(orderId).get();
        record.setStatus(Status.FINISH);
        orderRepository.save(record);
    }

    public void createNewOrder() {
        // สร้าง order ใหม่
        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setStatus(Status.ORDER);
        // บันทึก order ลงในฐานข้อมูล
        PurchaseOrder record = orderRepository.save(newOrder);
        currentOrderId = record.getId();
    }

    public void order(UUID menuId, AddCartRequest request) {
        if (currentOrderId == null)
            createNewOrder();
        // ดึง order ปัจจุบันมา
        PurchaseOrder currentOrder = orderRepository.findById(currentOrderId).get();
        // ดึงเมนูที่ลูกค้าสั่งมา
        Menu menu = menuRepository.findById(menuId).get();
        // สร้าง order item ขึ้นมา
        OrderItem item = new OrderItem();
        item.setId(new OrderItemKey(currentOrderId, menuId));
        item.setPurchaseOrder(currentOrder);
        item.setMenu(menu);
        item.setQuantity(request.getQuantity());
        itemRepository.save(item);
    }



}
