package jpabook.jpashop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private var member: Member? = null

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val orderItems: MutableList<OrderItem> = ArrayList()

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "delivery_id")
    private var delivery: Delivery? = null

    var orderDate : LocalDateTime? = null //주문 시간

    @Enumerated(EnumType.STRING)
    var status : OrderStatus? = null  //주문상태 [ORDER, CANCEL]

    //==연관관계 메서드==//
    fun setMember(member: Member) {
        this.member = member
        member.orders!!.add(this)
    }

    fun addOrderItem(orderItem: OrderItem) {
        orderItems.add(orderItem)
        orderItem.order = this
    }

    fun setDelivery(delivery: Delivery) {
        this.delivery = delivery
        delivery.order = this
    }
    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    fun cancel() {
        check(!(delivery!!.status === DeliveryStatus.COMP)) { "이미 배송완료된 상품은 취소가 불가능합니다." }
        status = OrderStatus.CANCEL
        for (orderItem in orderItems) {
            orderItem.cancel()
        }
    }
    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    fun getTotalPrice(): Int{
        var totalPrice = 0
        for (orderItem in orderItems) {
            totalPrice += orderItem.getTotalPrice()
        }
        return totalPrice
    }

    companion object {
        //==생성 메서드==//
        fun createOrder(member: Member, delivery: Delivery, vararg orderItems: OrderItem): Order {
            val order = Order()
            order.setMember(member)
            order.setDelivery(delivery)
            for (orderItem in orderItems) {
                order.addOrderItem(orderItem)
            }
            order.status = OrderStatus.ORDER
            order.orderDate = LocalDateTime.now()
            return order
        }
    }
}