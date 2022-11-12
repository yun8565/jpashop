package jpabook.jpashop.domain.item

import jpabook.jpashop.domain.Category
import jpabook.jpashop.exception.NotEnoughStockException
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
abstract class Item() {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    val id: Long? = null

    var name: String? = null
    var price = 0
    var stockQuantity = 0

    @ManyToMany(mappedBy = "items")
    val categories: List<Category> = ArrayList()

    //==비즈니스 로직==//
    /**
     * stock 증가
     */
    fun addStock(quantity: Int) {
        stockQuantity += quantity
    }

    /**
     * stock 감소
     */
    fun removeStock(quantity: Int) {
        val restStock = stockQuantity - quantity
        if (restStock < 0) {
            throw NotEnoughStockException("need more stock")
        }
        stockQuantity = restStock
    }
}