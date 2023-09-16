package ku.cs.kafe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor // เพื่อให้ generate constructor ที่ไม่มี parameter
@AllArgsConstructor // เพื่อให้ generate constructor ที่มี parameter ตาม field
@EqualsAndHashCode // เพื่อให้ generate equals() และ hashCode() ตาม field
@Embeddable // เพื่อบอกว่า class นี้จะถูกแทรกเข้าไปใน entity อื่น
public class OrderItemKey implements Serializable { // ต้อง implements Serializable เพื่อให้สามารถเก็บลง database ได้


    @Column(name = "order_id")
    private UUID orderId;


    @Column(name = "menu_id")
    private UUID menuId;
}
