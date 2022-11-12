package jpabook.jpashop.domain

import jpabook.jpashop.domain.item.Item
import javax.persistence.*

@Entity
class Category (
    val name: String? = null,

    @ManyToMany
    @JoinTable(
        name = "category_item",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    val items: List<Item> = ArrayList(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Category? = null,

    @OneToMany(mappedBy = "parent")
    val child: MutableList<Category> = ArrayList()
)   {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    val id: Long? = null

    //==연관관계 메서드==//
    fun addChildCategory(child: Category) {
        this.child.add(child)
        child.parent = this
    }
}