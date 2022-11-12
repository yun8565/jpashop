package jpabook.jpashop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jpabook.jpashop.domain.item.Item
import javax.persistence.*

@Entity
class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var item: Item? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null
    var orderPrice: Int? = null
    var count: Int? = null

    //==비즈니스 로직==//
    fun cancel() {
        count?.let { item?.addStock(it) }
    }
    //==조회 로직==//
    /**
     * 주문상품 전체 가격 조회
     */
    fun getTotalPrice(): Int {
        return orderPrice!!.times(count!!)
    }

    companion object {
        //==생성 메서드==//
        fun createOrderItem(item: Item, orderPrice: Int, count: Int): OrderItem {
            val orderItem = OrderItem()
            orderItem.item = item
            orderItem.orderPrice = orderPrice
            orderItem.count = count
            item.removeStock(count)
            return orderItem
        }
    }
}